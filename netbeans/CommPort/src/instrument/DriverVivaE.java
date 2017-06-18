package instrument;

import entity.Instrument;
import entity.Result;
import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.astm.*;
import instrument.vivaE.FrameVivaE;
import instrument.vivaE.MessageVivaE;
import instrument.vivaE.RecordProducerVivaE;
import instrument.vivaE.VivaeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelHost.Model;

public class DriverVivaE extends DriverInstrument {

    int counter = 0;
    private byte inputValueMainRun;
    private RecordProducer recordProducer = new RecordProducerVivaE();

    public DriverVivaE(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws InstrumentException {
        counter++;
        if (isStoppedMashine()) {
            throw new BreakException("mashine " + instrument.getName() + " stopped");
        }
        try {
            threadSleep(12000);
//            threadSleep(1000);
            /*
            //for testing of long messages
            if (counter == 1) {
                String str =tempMethodGetLongString();
                throw new InstrumentException("test exception: " + str);
            }
            */
            
            if (inputStream.available() == 0) {

//                if (testMode && counter % 150 == 0) {
                //через каждые 30 мин
                if ((counter == 1) || (counter % 150 == 0)) {
                    sendTestASTMEnq();
                }
                if (instrument.getMode() == Instrument.ModeWorking.BATCH) {
                    sendWorkOrdersBatch();
                }
                return;
            }
            inputValueMainRun = inputStreamRead();
            if (inputValueMainRun == ENQ) {
                outputStreamWrite(ACK);
                inputValueMainRun = inputStreamRead();
                if (inputValueMainRun == STX) {
                    MessageAstm message = getMessage();
                    TypeMessage type = message.getTypeMessage();
                    switch (type) {
                        case QUERY_FROM_INSTRUMENT:
                            String sid = message.getSidFromQueryRecord();
                            logTemp("info", "typeMessage: Query_FROM_INSTRUMENT, sid " + sid, true); //todo comment after
                            if (sid.equals("ALL")) {
                                log("info", "operator of apparatus requested Worklist", true);
                                sendWorkOrdersBatch();
                            } else {
                                WorkOrder wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());

                                if (testMode) {
                                    logTemp("info", "wo = " + wo, true); //todo comment after
                                }

                                if (sendWorkOrder(wo, false)) {
                                    model.setWorkOrderAsServed(wo.getId());
                                }
                            }
                            break;
                        case RESULT_FROM_INSTRUMENT:
                            logTemp("info", "typeMessage: RESULT_FROM_INSTRUMENT", true);
                            saveResults(message);
                            break;
                    }
                }
            } else {
                logTemp("info", "Other than ENQ: " + getASCIICodeAsString(inputValueMainRun), true);
            }
        } catch (Exception ex) {
           throw new InstrumentException(ex);
        }

    }

    @Override
    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        try {
            if (workOrder != null) {
                MessageAstm messageToSent = recordProducer.createMessageFromWorkOrder(workOrder);
                List<Frame> frames = messageToSent.getFrames();
                outputStreamWrite(ENQ);
                inputValueMainRun = inputStreamRead();
                for (int i = 0; i < frames.size(); i++) {
                    Frame frame = frames.get(i);
                    outputStreamWrite(frame.getRawBytes());
                    inputValueMainRun = inputStreamRead();
                    if (inputValueMainRun == NACK) {
                        logTemInfo("Receiving NACK from Instrument");
                        break;
                    } else if (inputValueMainRun != ACK) {
                        logTemInfo("Receiving Not ACK from Instrument");
                        break;
                    }
                }
                outputStreamWrite(EOT);
                return true;

            } else {
                logTemInfo("Worder is not avalable!!!!!!");
                outputStreamWrite(ENQ);
                inputValueMainRun = inputStreamRead();
                if (inputValueMainRun == ACK) {
                    outputStreamWrite(EOT);
                }
            }
        } catch (IOException ex) {
            throw new VivaeException(ex);
        }
        return false;
    }

    @Override
    public List<Result> getResultsFromMessage(Object messageObj) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        //RecordProducer recordProducer = new RecordProducerVivaE();
        try {
            MessageAstm message = (MessageAstm) messageObj;
            List<Record> listRecords = message.getRecords();
            String sid = "";
            for (Record record : listRecords) {
                if (record.getTypeRecord() == TypeRecord.ORDER) {
                    sid = recordProducer.getSidFromOrderRecord(record);
                } else if (record.getTypeRecord() == TypeRecord.RESULT) {
                    RecordResult recordResult = (RecordResult) recordProducer.getResultRecord(record);

                    Result result = new Result();
                    String testCode = recordResult.getTestCode();
                    result.setSid(sid);
                    result.setTestCode(testCode);
                    result.setInstrument(instrument.getName());
                    String addParams = "";

                    result.setValue(recordResult.getValue());
                    result.setUnits(recordResult.getUnits());
                    result.setReferenseRanges(recordResult.getRefRanges());
                    result.setAbnormalFlags(recordResult.getAbnormalFlags());
                    //addParams = "natureAbnormalityTesting" + "=" + recordResult.getNatureAbnormalityTesting();
                    addParams += "resultStatus" + "=" + recordResult.getResultStatus();
                    //addParams += ",dilutionProtocol" + "=" + recordResult.getDilutionProtocol();
                    //addParams += ",dilutionRatio" + "=" + recordResult.getDilutionRatio();
                    //addParams += ",replicateNumber" + "=" + recordResult.getReplicateNumber();
                    //addParams += ",resultAspects" + "=" + recordResult.getResultAspects();
                    addParams += ",dateTestCompleted" + "=" + recordResult.getDateTestCompleted();
                    result.setAddParams(addParams);

                    entity.Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                    result.setParameterId(test.getParameter().getId());
                    WorkOrder wo = model.getWorkOrderBySidAndInstrument(result.getSid(), this.instrument.getId());
                    if (wo != null) {
                        result.setWorkOrderId(wo.getId());
                    }
                    int lastVersion = model.getVersionOfLastResultByInstrumentAndSid(instrument.getName(), sid, testCode);
                    result.setVersion(lastVersion + 1);
                    list.add(result);
                }
            }
        } catch (Exception ex) {
            throw new VivaeException(ex);
        }
        return list;
    }

    @Override
    public void saveResults(Object inputMessage) throws InstrumentException {
        try {
            List<Result> listResults = getResultsFromMessage(inputMessage);
            for (Result result : listResults) {
                model.insertObject(result);
            }
        } catch (Exception ex) {
            throw new VivaeException(ex);
        }
    }

    private MessageAstm getMessage() throws VivaeException {
        List<Frame> listFrames = new ArrayList<>();
        MessageAstm mess = null;
        try {
            do {
                Frame frame = readFrame();
                log(TO_HOST, frame.getFrameAsString(), true);
                if (!frame.validateCheckSum()) {
                    log("info", "checkSum:" + frame.getCheckSum() + ", calculated: "
                            + ((FrameVivaE) frame).getCalculatedCheckSum(), true);
                }
                if (1 == 0) {
                    //if (!frame.validateCheckSum()) {
                    outputStream.write(NACK);
                    log(TO_INSTRUMENT, "NACK", true);
                } else {
                    listFrames.add(frame);
                    outputStreamWrite(ACK);
                    inputValueMainRun = (byte) inputStreamRead();
                    if (inputValueMainRun != STX && inputValueMainRun != EOT) {
                        logTemInfo("input value not STX and not EOT");
                        break;
                    }
                }
            } while (inputValueMainRun != EOT);
            List<Record> records = recordProducer.getRecordsFromFrames(listFrames);
            mess = new MessageVivaE(records);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new VivaeException(ex);
        }
        return mess;
    }

    private Frame readFrame() throws VivaeException {
        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = ASCII.STX;
        byte[] neewByteArray;
        Frame frame;
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
                    byteArray[indexOfByteArray++] = incomeValue;//ETX or ETB read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read();//chk 1 read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // chk 2 read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // CR read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // LF read
                    neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                    frame = new FrameVivaE(new String(neewByteArray));
                    break;
                }
            } catch (IOException ex) {
                throw new VivaeException(ex);
            }
        }
        return frame;
    }

    private static  String tempMethodGetLongString() {
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < 65500; i++) {
            stb.append(i+"-");
            if (i % 50 == 0) {
                stb.append("<br>");
            }

        }
        String str = stb.toString();
        return stb.toString();
    }

}
