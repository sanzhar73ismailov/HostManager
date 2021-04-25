package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.instrument.astm.TypeMessage;
import kz.biostat.lishostmanager.comport.instrument.cobas411.*;
import kz.biostat.lishostmanager.comport.modelHost.DaoException;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.io.InputStream;
import java.io.OutputStream;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class DriverCobas411 extends DriverInstrument {

//    public static final String HOST_NAME = "192.168.127.253";
    //public static final String HOST_NAME = "localhost";
    //public static final int PORT_NUMBER = 950;
    // public static final int PORT_NUMBER = 4221;
    // public static final String INSTRUMENT_NAME = "cobas411";
    int attempt = 1;
    //int workRegime = 1;
    public static int WORK_REGIME_NO_BARCODE = 1;
    public static int WORK_REGIME_YES_BARCODE = 2;
    byte inputValueMainRun;
    private static final int TIMES_TO_SEND_ENQ = 6;

    public DriverCobas411(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws InstrumentException {
        if (isStoppedMashine()) {
            throw new BreakException();
        }
        if (instrument.getMode() == Instrument.ModeWorking.BATCH) {
            logTemInfo("Check if exist workorders to send");
            sendWorkOrdersBatch();
        }
        logTemInfo("\nIdle status..." + new Date());
        try {
            Thread.sleep(10000);
            if (inputStream.available() == 0) {
//                throw new ContinueException();
                return;
            }
            inputValueMainRun = (byte) inputStream.read();
            log(TO_HOST, ASCII.getASCIICodeAsString(inputValueMainRun), true);
            if (inputValueMainRun == ENQ) {
                Thread.sleep(25);
                outputStream.write(ACK);
                log(TO_INSTRUMENT, "ACK", true);
                inputValueMainRun = (byte) inputStream.read();
                if (inputValueMainRun == STX) {
                    MessageCobas411 inputMessage = getMessage();
                    if (inputMessage == null) {
                        //throw new ContinueException();
                        return;
                    }
                    logTemInfo("type Of Message: " + inputMessage.getTypeMessage());
                    TypeMessage typeMessage = inputMessage.getTypeMessage();

                    switch (typeMessage) {
                        case QUERY_FROM_INSTRUMENT:
                            logTemInfo("typeMessage: QUERY_FROM_INSTRUMENT");
                            int idWorkOrder = 0;
                            WorkOrder wo = getWorkOrder(inputMessage);
                            if (wo != null) {
                                idWorkOrder = wo.getId();
                                boolean sendWo = sendWorkOrderReply(wo);
                                logTemInfo("sendWorkOrderReply = " + sendWo);
                                if (sendWo) {
                                    model.setWorkOrderAsServed(idWorkOrder);
                                }
                            } else {
                                boolean sendWo = sendNoWorkOrder(inputMessage);
                                if (sendWo) {
                                    logTemInfo("Unknown Order info was sent to Instrument");
                                }
                            }
                            break;
                        case RESULT_FROM_INSTRUMENT:
                            logTemInfo("typeMessage: RESULT_FROM_INSTRUMENT");
                            saveResults(inputMessage);
                            break;
                        default:
                            logTemInfo("typeMessage: UNKNOWN");
                            break;
                    }
                } else {
                    log(TO_HOST, ASCII.getASCIICodeAsString(inputValueMainRun), true);
                }
            }

        } catch (ModelException | SQLException | InterruptedException | DaoException | IOException ex) {
            throw new InstrumentException(ex);
        }
    }

    @Override
    public List<Result> getResultsFromMessage(Object messageObj) throws Cobas411Exception {
        MessageCobas411 message = (MessageCobas411) messageObj;
        List<Result> list = new ArrayList<>();
        try {
            RecordCobas411 orderRecord = message.getOrderRecord();
            ParamsCobas411OrderRecord paramsOrder = orderRecord.getParamsFromOrderResultRecord();
            WorkOrder wo;
            List<RecordCobas411> recordsWithResults = message.getResultsRecords();
            for (RecordCobas411 record : recordsWithResults) {
                //R|1|^^^12/1/not|-1^0.101|COI||N||F|||||E1<CR>
                String[] tokens = record.getTextRecord().split("\\|");
                //////////////////////
                String[] universaltestIdDilutionPredilution = tokens[2].split("\\^");
                String[] testIdDilutionPredilution = universaltestIdDilutionPredilution[3].split("\\/");
                String testCode = testIdDilutionPredilution[0];
                String dilution = testIdDilutionPredilution[1];
                String preDilution = "";
                if (testIdDilutionPredilution.length == 3) {
                    preDilution = testIdDilutionPredilution[2];
                }
                //////////
                String[] dataMeasurementValue = tokens[3].split("\\^");
                String value = dataMeasurementValue[0];
                String messageValueOrCutIndex = "";
                if (dataMeasurementValue.length > 1) {
                    messageValueOrCutIndex = dataMeasurementValue[1];
                }
                //////
                String units = tokens[4];
                //////
                String refRanges = tokens[5];
                /////
                String abnormalFlags = tokens[6];
                /////////
                String initialOrRerunStatus = tokens[8];

                Result result = new Result();
                result.setTestCode(testCode);
                result.setValue(value);
                result.setUnits(units);
                result.setReferenseRanges(refRanges);
                result.setAbnormalFlags(abnormalFlags);
                result.setInitialRerun(initialOrRerunStatus);
                result.setRawText(record.getTextRecord());
                String addParams = "";
                addParams += "dilution=" + dilution + ",";
                addParams += "preDilution=" + preDilution + ",";
                addParams += "messageValueOrCutIndex=" + messageValueOrCutIndex + "";
                result.setAddParams(addParams);
                Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                wo = model.getWorkOrderBySidAndInstrument(paramsOrder.getSid(), this.instrument.getId());
                result.setParameterId(test.getParameter().getId());
                if (wo != null) {
                    result.setWorkOrderId(wo.getId());
                } else {
                    throw new ModelException("no workorder with sid "
                            + paramsOrder.getSid() + " and instrument " + this.instrument.getName());
                }
                list.add(result);
            }
        } catch (Exception ex) {
            throw new Cobas411Exception(ex);
        }
        return list;
    }

    private MessageCobas411 getMessage() throws Cobas411Exception {
        return getMessage(inputStream, outputStream, inputValueMainRun);
    }

    public MessageCobas411 getMessage(InputStream inputStream, OutputStream outputStream, byte inputValue) throws Cobas411Exception {
        List<FrameCobas411> listFrames = getFrames(inputStream, outputStream, inputValue);
        List<RecordCobas411> records = null;
        if (listFrames == null) {
            return null;
        }
        logTemInfo("After get Frames");
        records = getRecords(listFrames);
        logTemInfo("After getRecords");
        MessageCobas411 inputMessage = new MessageCobas411(records);
        return inputMessage;

    }

    public List<FrameCobas411> getFrames(InputStream inputStream, OutputStream outputStream, int firstValue) throws Cobas411Exception {
        List<FrameCobas411> listFrames = new ArrayList<>();
        FrameCobas411 currentFrame = null;
        boolean readNextFrame = false;
        int sendNackCounter = 0;
        try {
            do {
                currentFrame = readFrame(inputStream, firstValue);
                logTemInfo(new Date() + ", Framme N" + currentFrame.getNumberFrame());
                log(TO_HOST, currentFrame.getFrameAsString(), true);
                if (currentFrame.checkChk()) {
                    outputStream.write(ACK);
                    log(TO_INSTRUMENT, "ACK", true);
                    listFrames.add(currentFrame);
                    if (!currentFrame.isLastFrame()) {
                        if (inputStream.read() == STX) {
                            readNextFrame = true;
                        } else {
                            readNextFrame = false;
                            logTemInfo("Error---------");
                        }
                    } else {
                        readNextFrame = false;
                        if (inputStream.read() == EOT) {
                            log(TO_INSTRUMENT, "EOT", true);
                        }
                    }
                } else {

                    outputStream.write(NACK);
                    log(TO_INSTRUMENT, "NACK", true);
                    sendNackCounter++;
                    if (sendNackCounter == 6) {
                        return null;
                    }
                    if (inputStream.read() == STX) {
                        readNextFrame = true;
                        continue;
                    }

                }
            } while (readNextFrame);
        } catch (IOException ex) {
            throw new Cobas411Exception(ex);
        }
        return listFrames;
    }

    public List<RecordCobas411> getRecords(List<FrameCobas411> listFrames) throws Cobas411Exception {
        List<RecordCobas411> records = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (FrameCobas411 frame : listFrames) {
            stringBuilder.append(frame.getText());
        }
        String recordsAsText = stringBuilder.toString();
        // logTemInfo("recordsAsText = " + recordsAsText);
        String[] strArray = recordsAsText.split("\r");
        //logTemInfo("strArray.length = " + strArray.length);
        for (String string : strArray) {
            try {
                records.add(new RecordCobas411(string));
                //logTemInfo("string = " + string);
            } catch (InstrumentException ex) {
                throw new Cobas411Exception(ex);
            }
        }
        return records;
    }

    public static FrameCobas411 readFrame(InputStream inputStream, int firstValue) throws Cobas411Exception {
        if (firstValue != ASCII.STX) {
            return null;
        }
        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = ASCII.STX;
        byte[] neewByteArray;
        FrameCobas411 frame;
        while (true) {
            try {
                byte incomeValue = (byte) inputStream.read();
                if (sizeArray == indexOfByteArray) {
                    sizeArray *= 2;
                    byte[] tempArray = new byte[sizeArray];
                    System.arraycopy(byteArray, 0, tempArray, 0, byteArray.length);
                    byteArray = tempArray;
                }
                if (incomeValue != ASCII.ETX && incomeValue != ASCII.ETB) {
                    byteArray[indexOfByteArray++] = incomeValue;
                } else {
                    byteArray[indexOfByteArray++] = incomeValue;//ETB or ETX read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read();//chk 1 read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // chk 2 read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // CR read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // LF read
                    neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                    frame = new FrameCobas411(neewByteArray);
                    break;
                }
            } catch (IOException ex) {
                throw new Cobas411Exception(ex);
            }
        }
        return frame;
    }

    public void sendFrame(OutputStream outputStream, FrameCobas411 frame) throws IOException {
        logTemInfo("Start sending frame");
        outputStream.write(frame.getRawBytes());
        outputStream.flush();
        logTemInfo("After sending frame");
    }

    @Deprecated
    private int[] getIntArrayFromCsvString(String str) {
        int[] retArrayInt = new int[0];
        if (str != null && !str.isEmpty()) {
            String[] strArray = str.split(",");
            retArrayInt = new int[strArray.length];
            for (int i = 0; i < retArrayInt.length; i++) {
                retArrayInt[i] = Integer.parseInt(strArray[i]);

            }
        }
        return retArrayInt;
    }

    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        try {
            if (!initialSendEnqToInstrument()) {
                return false;
            }
            byte inputValue = 0;
            ParamsCobas411OrderRecord paramsOrder = new ParamsCobas411OrderRecord(workOrder);
            MessageCobas411 messageToSend = MessageCobas411.createOrderMessage(paramsOrder, batchOrder);
            List<FrameCobas411> framesToSend = messageToSend.getFramesFromMessage();
            logTemInfo("framesToSend.size() = " + framesToSend.size());
            // send frames
            for (int j = 0; j < framesToSend.size(); j++) {
                logTemInfo("frame cycle: " + (j + 1) + " from " + framesToSend.size());
                FrameCobas411 frame = framesToSend.get(j);
                sendFrame(outputStream, frame);
                log(TO_INSTRUMENT, frame.getFrameAsString(), true);
                inputValue = (byte) inputStream.read();
                log(TO_HOST, ASCII.getASCIICodeAsString(inputValue), true);
                if (inputValue == ACK) {
                    if ((j + 1) == framesToSend.size()) {
                        outputStream.write(EOT);
                        log(TO_INSTRUMENT, "EOT", true);
                        return true;
                    }
                } else {
                    logTemInfo("H<-M NOT ACK received from Instrument:" + inputValue);
                    break;
                }
            } //end for list orders
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
        return false;
    }

    public boolean sendNoWorkOrder(MessageCobas411 message) throws Cobas411Exception, InterruptedException, DaoException, IOException {
        if (!initialSendEnqToInstrument()) {
            return false;
        }
        byte inputValue = 0;
        RecordCobas411 queryRecord = message.getQueryRecord();
        ParamsCobas411OrderRecord params = queryRecord.getParamsFromQueryRecord();
        RecordCobas411 recordHeader = RecordCobas411.createHeaderRecordFromHostOrderReply();
        RecordCobas411 recordPatient = RecordCobas411.createPatientRecordFromHost();
        RecordCobas411 recordNoOrder = RecordCobas411.createOrderRecordFromHost(params);
        RecordCobas411 recordTerminal = RecordCobas411.createTerminalRecordFromHost();
        List<RecordCobas411> listRecords = new ArrayList<>();
        listRecords.add(recordHeader);
        listRecords.add(recordPatient);
        listRecords.add(recordNoOrder);
        listRecords.add(recordTerminal);
        MessageCobas411 messageToSend = new MessageCobas411(listRecords);
        FrameCobas411 frame = messageToSend.getFramesFromMessage().get(0);
        sendFrame(outputStream, frame);
        log(TO_INSTRUMENT, frame.getFrameAsString(), true);
        inputValue = (byte) inputStream.read();
        log(TO_HOST, ASCII.getASCIICodeAsString(inputValue), true);
        if (inputValue == ACK) {
            outputStream.write(EOT);
            log(TO_INSTRUMENT, "EOT", true);
            return true;
        }
        return false;
    }

    private boolean initialSendEnqToInstrument() throws IOException, InterruptedException, DaoException {
        int attemptToSendENQ = 0;
        byte inputValue = 0;
        while (attemptToSendENQ++ < TIMES_TO_SEND_ENQ) {
            System.out.print(attemptToSendENQ + " attempt, ");
            if (attemptToSendENQ % 20 == 0) {
                //
            }
            outputStream.write(ENQ);
            log(TO_INSTRUMENT, "ENQ", true);
            Thread.sleep(2000);
            if (inputStream.available() > 0) {
                inputValue = (byte) inputStream.read();
                log(TO_HOST, ASCII.getASCIICodeAsString(inputValue), true);
                if (inputValue == ACK) {
                    return true;
                } else if (inputValue == ENQ) {
                    logTemInfo("H<-M ENQ - Link Contention");
                    outputStream.write(NACK);
                    log(TO_INSTRUMENT, "NACK", true);
                    return false;
                }
            }
        }
        logTemInfo("No ACK to ENQ after " + attemptToSendENQ + " times, go to Idle Status");
        return false;
    }

    private void saveResults(MessageCobas411 inputMessage) throws Cobas411Exception {
        try {

            List<Result> listResults = getResultsFromMessage(inputMessage);
            for (Result result : listResults) {
                model.insertObject(result);
            }
        } catch (Exception ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public WorkOrder getWorkOrder(MessageCobas411 message) throws Cobas411Exception, SQLException, ModelException {
        Model model = new ModelImpl();
        WorkOrder workOrder = null;
        RecordCobas411 queryRecord = message.getQueryRecord();
        ParamsCobas411OrderRecord paramsQuery = queryRecord.getParamsFromQueryRecord();
        if (paramsQuery.getSid() == null || paramsQuery.getSid().startsWith("@")) {
            logTemInfo("Unknown sid, search workorder by rack and postion");
            workOrder = model.getWorkOrderByRackPositionInstrumentDateWithFreeStatus(paramsQuery.getRack(), Integer.parseInt(paramsQuery.getPosition()), instrument.getId(), new Date());
        } else {
            logTemInfo("Receive SID, search workorder by SID");
            workOrder = model.getWorkOrderBySidInstrumentWithFreeStatus(paramsQuery.getSid(), instrument.getId());
        }
        return workOrder;
    }

    @Override
    public void saveResults(Object inputMessage) throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
