package instrument;

import entity.Instrument;
import entity.Result;
import entity.WorkOrder;
import static instrument.ASCII.*;
import static instrument.DriverInstrument.TO_HOST;
import instrument.astm.Frame;
import instrument.astm.MessageAstm;
import instrument.astm.TypeMessage;
import instrument.astm.TypeRecord;
import instrument.immulite2000.FrameImmulite2000;
import instrument.immulite2000.Immulite2000Exception;
import instrument.immulite2000.MessageImmulite2000;
import instrument.astm.Record;
import instrument.astm.RecordProducer;
import instrument.immulite2000.RecordProducerImmulite2000;
import instrument.immulite2000.RecordResultImmulite2000;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelHost.Model;

public class DriverImmulite2000 extends DriverInstrument {

    private byte inputValueMainRun;
    public static final String PASSWORD_INSTR = "DPC";
    public static final String SENDER_ID = "Sender";
    public static final String RECEIVER_ID = "Receiver";
    int counter = 0;
    RecordProducer recordProducer = new RecordProducerImmulite2000();

    public DriverImmulite2000(Instrument instrument, Model model) {
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
            if (inputStream.available() == 0) {
                if (instrument.getMode() == Instrument.ModeWorking.BATCH) {
                    if (testMode) {
                        logTemInfo("Check if exist workorders to send");
                    }
                    sendWorkOrdersBatch();
                }
//                if (testMode && counter % 150 == 0) {
                //через каждые 30 мин
                if ((counter == 1) || (counter % 150 == 0)) {
                    sendTestASTMEnq();
                }
                return;
            }
            inputValueMainRun = inputStreamRead();
            if (inputValueMainRun == ENQ) {
                outputStreamWrite(ACK);
                inputValueMainRun = inputStreamRead();
                if (inputValueMainRun == STX) {
                    MessageImmulite2000 message = getMessage();

                    TypeMessage type = message.getTypeMessage();
                    switch (type) {
                        case QUERY_FROM_INSTRUMENT:
                            //logTemInfo("typeMessage: Query_FROM_INSTRUMENT");
                            String sid = message.getSidFromQueryRecord();
                            //logTemInfo("sid = " + sid);
                            WorkOrder wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());
                            //logTemInfo("wo = " + wo);
                            if (sendWorkOrder(wo, false)) {
                                model.setWorkOrderAsServed(wo.getId());
                            }
                            break;
                        case RESULT_FROM_INSTRUMENT:
                            //logTemInfo("typeMessage: RESULT_FROM_INSTRUMENT");
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
    public void saveResults(Object inputMessage) throws Immulite2000Exception {
        try {
            List<Result> listResults = getResultsFromMessage(inputMessage);
            for (Result result : listResults) {
                model.insertObject(result);
            }
        } catch (Exception ex) {
            throw new Immulite2000Exception(ex);
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
            throw new Immulite2000Exception(ex);
        }
        return false;
    }

    @Override
    public List<Result> getResultsFromMessage(Object messageObj) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        RecordProducer recordProducer = new RecordProducerImmulite2000();
        try {
            MessageImmulite2000 message = (MessageImmulite2000) messageObj;
            List<Record> listRecords = message.getRecords();
            String sid = "";
            for (Record record : listRecords) {
                if (record.getTypeRecord() == TypeRecord.ORDER) {
                    sid = recordProducer.getSidFromOrderRecord(record);
                } else if (record.getTypeRecord() == TypeRecord.RESULT) {
                    RecordResultImmulite2000 recordResult = (RecordResultImmulite2000) recordProducer.getResultRecord(record);
                    Result result = new Result();
                    String testCode = recordResult.getTestCode().replace("^^^", "");
                    result.setSid(sid);
                    result.setTestCode(testCode);
                    result.setInstrument(instrument.getName());
                    String addParams = "";

                    result.setValue(recordResult.getValue());
                    result.setUnits(recordResult.getUnits());
                    result.setReferenseRanges(recordResult.getRefRanges());
                    result.setAbnormalFlags(recordResult.getAbnormalFlags());
                    addParams = "natureAbnormalityTesting" + "=" + recordResult.getNatureAbnormalityTesting();
                    addParams += ",resultStatus" + "=" + recordResult.getResultStatus();
                    result.setAddParams(addParams);

                    entity.Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                    if(test == null){
                        log("info", "unknown test code: " + testCode, true);
                        continue;
                    }
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
            ex.printStackTrace();
            throw new Immulite2000Exception(ex);
        }
        return list;
    }

    private MessageImmulite2000 getMessage() throws Immulite2000Exception {
        MessageImmulite2000 mess = null;
        try {
            List<Frame> listFrames = new ArrayList<>();
            do {
                FrameImmulite2000 frame = readFrame();
                log(TO_HOST, frame.getFrameAsString(), true);
                if (!frame.validateCheckSum()) {
                    logTemInfo("Frame not good, checksum wrong");
                    outputStream.write(NACK);
                    log(TO_INSTRUMENT, "NACK", true);
                } else {
                    listFrames.add(frame);
                    outputStream.write(ACK);
                    log(TO_INSTRUMENT, "ACK", true);
                    inputValueMainRun = (byte) inputStream.read();
                    log(TO_HOST, ASCII.getASCIICodeAsString(inputValueMainRun), true);
                    if (inputValueMainRun != STX && inputValueMainRun != EOT) {
                        logTemInfo("input value not STX and not EOT: " + ASCII.getASCIICodeAsString(inputValueMainRun));
                        break;
                    }
                }
            } while (inputValueMainRun != EOT);

            List<Record> records = recordProducer.getRecordsFromFrames(listFrames);
            mess = new MessageImmulite2000(records);
        } catch (Exception ex) {
            throw new Immulite2000Exception(ex);
        }
        return mess;
    }

    public FrameImmulite2000 readFrame() throws Immulite2000Exception {

        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = ASCII.STX;
        byte[] neewByteArray;
        FrameImmulite2000 frame;
        while (true) {
            try {
                byte incomeValue = (byte) inputStream.read();
                if (sizeArray == indexOfByteArray) {
                    sizeArray *= 2;
                    byte[] tempArray = new byte[sizeArray];
                    System.arraycopy(byteArray, 0, tempArray, 0, byteArray.length);
                    byteArray = tempArray;
                }
                if (incomeValue != ASCII.ETX) {
                    byteArray[indexOfByteArray++] = incomeValue;
                } else {
                    byteArray[indexOfByteArray++] = incomeValue;//ETX read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read();//chk 1 read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // chk 2 read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // CR read
                    byteArray[indexOfByteArray++] = (byte) inputStream.read(); // LF read
                    neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                    frame = new FrameImmulite2000(new String(neewByteArray));
                    break;
                }
            } catch (IOException ex) {
                throw new Immulite2000Exception(ex);
            }
        }
        return frame;
    }

}
