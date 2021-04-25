package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import kz.biostat.lishostmanager.comport.instrument.astm.MessageAstm;
import kz.biostat.lishostmanager.comport.instrument.astm.Frame;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordProducer;
import kz.biostat.lishostmanager.comport.instrument.astm.TypeMessage;
import kz.biostat.lishostmanager.comport.instrument.astm.TypeRecord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kz.biostat.lishostmanager.comport.instrument.adviaCentaurCP.*;
import kz.biostat.lishostmanager.comport.modelHost.Model;

public class DriverAdviaCentaurCP extends DriverInstrument {

    int counter = 0;
    private byte inputValueMainRun;
    private RecordProducer recordProducer = new RecordProducerAdviaCentaurCp();

    public DriverAdviaCentaurCP(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws InstrumentException, IOException {
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
                    MessageAstm message = getMessage();
                    TypeMessage type = message.getTypeMessage();
                    switch (type) {
                        case QUERY_FROM_INSTRUMENT:
                            logTemInfo("typeMessage: Query_FROM_INSTRUMENT"); //todo comment after
                            String sid = message.getSidFromQueryRecord();
                            if (sid.equals("ALL")) {
                                log("info", "operator of apparatus requested Worklist", true);
                                sendWorkOrdersBatch();
                            } else {

                                if (testMode) {
                                    logTemInfo("sid = " + sid); //todo comment after
                                }

                                WorkOrder wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());

                                if (testMode) {
                                    logTemInfo("wo = " + wo); //todo comment after
                                }

                                if (sendWorkOrder(wo, false)) {
                                    model.setWorkOrderAsServed(wo.getId());
                                }
                            }
                            break;
                        case RESULT_FROM_INSTRUMENT:
                            logTemInfo("typeMessage: RESULT_FROM_INSTRUMENT");
                            saveResults(message);
                            break;
                    }
                }

            } else {
                logTemp("info", "Other than ENQ: " + getASCIICodeAsString(inputValueMainRun), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
            throw new AdviaCentaurException(ex);
        }
        return false;
    }

    @Override
    public List<Result> getResultsFromMessage(Object messageObj) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        RecordProducer recordProducer = new RecordProducerAdviaCentaurCp();
        try {
            MessageAstm message = (MessageAstm) messageObj;
            List<Record> listRecords = message.getRecords();
            String sid = "";
            for (Record record : listRecords) {
                if (record.getTypeRecord() == TypeRecord.ORDER) {
                    sid = recordProducer.getSidFromOrderRecord(record);
                } else if (record.getTypeRecord() == TypeRecord.RESULT) {
                    RecordResultAdviaCentaurCP recordResult = (RecordResultAdviaCentaurCP) recordProducer.getResultRecord(record);
                    if (!recordResult.isDose() || recordResult.isIndex()) {
                        if (testMode) {
                            log("info", "result not dose and not index: " + recordResult.toString()
                                    + " and will not be saved in DB as result", true);

                        }
                    } else {
                        Result result = new Result();
                        String testCode = recordResult.getTestCode();
                        result.setSid(sid);
                        result.setTestCode(testCode);
                        result.setInstrument(instrument.getName());
                        String addParams = "";
                        String value = recordResult.getValue();
                        if(value != null){
                            value = value.replaceAll(" ", "");
                        }
                        result.setValue(value);
                        result.setUnits(recordResult.getUnits());
                        result.setReferenseRanges(recordResult.getRefRanges());
                        result.setAbnormalFlags(recordResult.getAbnormalFlags());
                        addParams = "natureAbnormalityTesting" + "=" + recordResult.getNatureAbnormalityTesting();
                        addParams += ",resultStatus" + "=" + recordResult.getResultStatus();
                        addParams += ",dilutionProtocol" + "=" + recordResult.getDilutionProtocol();
                        addParams += ",dilutionRatio" + "=" + recordResult.getDilutionRatio();
                        addParams += ",replicateNumber" + "=" + recordResult.getReplicateNumber();
                        addParams += ",resultAspects" + "=" + recordResult.getResultAspects();
                        addParams += ",dateTestCompleted" + "=" + recordResult.getDateTestCompleted();
                        result.setAddParams(addParams);

                        Test test = model.getTestByTestCode(testCode, this.instrument.getId());
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
            }
        } catch (Exception ex) {
            throw new AdviaCentaurException(ex);
        }
        return list;
    }

    private MessageAstm getMessage() throws AdviaCentaurException, IOException {
        List<Frame> listFrames = new ArrayList<>();
        MessageAdviaCentaurCp mess = null;
        try {
            do {
                Frame frame = readFrame();
                log(TO_HOST, frame.getFrameAsString(), true);
                if (!frame.validateCheckSum()) {
                    outputStream.write(NACK);
                    log(TO_INSTRUMENT, "NACK", true);
                } else {
                    logTemInfo("Frame good");
                    listFrames.add(frame);
                    outputStream.write(ACK);
                    log(TO_INSTRUMENT, "ACK", true);
                    inputValueMainRun = (byte) inputStream.read();
                    log(TO_HOST, ASCII.getASCIICodeAsString(inputValueMainRun), true);
                    if (inputValueMainRun != STX && inputValueMainRun != EOT) {
                        logTemInfo("input value not STX and not EOT");
                        break;
                    }
                }
            } while (inputValueMainRun != EOT);

            List<Record> records = recordProducer.getRecordsFromFrames(listFrames);
            mess = new MessageAdviaCentaurCp(records);
        } catch (Exception ex) {
            throw new AdviaCentaurException(ex);
        }
        return mess;
    }

    private Frame readFrame() throws AdviaCentaurException {
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
                    frame = new FrameAdviaCentaurCp(new String(neewByteArray));
                    break;
                }
            } catch (IOException ex) {
                throw new AdviaCentaurException(ex);
            }
        }
        return frame;

    }

    @Override
    public void saveResults(Object inputMessage) throws InstrumentException {
        try {
            List<Result> listResults = getResultsFromMessage(inputMessage);
            for (Result result : listResults) {
                model.insertObject(result);
            }
        } catch (Exception ex) {
            throw new AdviaCentaurException(ex);
        }
    }

    public List<Record> getRecordsFromFrames1(List<Frame> listFrames) throws AdviaCentaurException {
        List<Record> records = new ArrayList<>();
        try {
            StringBuilder stb = new StringBuilder();
            String recordText = "";
            for (Frame frame : listFrames) {
                String frameText = frame.getText();
                //Сзади убираем 6 символов <CR><ETX>CHK1CHK2<CR><LF> (вместо <ETX> может быть <ETB>)
                String str = frameText.substring(2, frameText.length() - 6);
                recordText += str;
                if (frame.isEtxFrame()) {
                    Record record = new Record(recordText);
                    records.add(record);
                    recordText = "";
                }

            }
        } catch (Exception ex) {
            throw new AdviaCentaurException(ex);
        }
        return records;
    }
    
}
