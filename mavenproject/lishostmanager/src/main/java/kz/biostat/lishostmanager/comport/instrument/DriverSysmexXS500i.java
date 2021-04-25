package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kz.biostat.lishostmanager.comport.instrument.astm.*;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.sysmexXS500i.*;
import kz.biostat.lishostmanager.comport.modelHost.Model;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.getASCIICodeAsString;

/**
 *
 * @author sanzh
 */
public class DriverSysmexXS500i extends DriverInstrument {

    private byte inputValueMainRun;
    int counter = 0;
    RecordProducer recordProducer = new RecordProducerSysmexXS500i();

    public DriverSysmexXS500i(Instrument instrument, Model model) {
        super(instrument, model);
    }

    
    @Override
    public void driverRun() throws InstrumentException, IOException {
        counter++;
        if (isStoppedMashine()) {
            throw new BreakException("mashine " + instrument.getName() + " stopped");
        }
        try {
            threadSleep(5000);
            if (inputStream.available() == 0) {
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
                    MessageSysmexXS500i message = getMessage();
                    TypeMessage type = message.getTypeMessage();
                    if(type == TypeMessage.RESULT_FROM_INSTRUMENT) {
                        //logTemInfo("typeMessage: RESULT_FROM_INSTRUMENT");
                        saveResults(message);
                    }
                }
            } else {
                logTemp("info", "Other than ENQ: " + getASCIICodeAsString(inputValueMainRun), true);
            }

        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
    }

    private MessageSysmexXS500i getMessage() throws SysmexXS500iException {
        MessageSysmexXS500i mess = null;
        try {
            List<Frame> listFrames = new ArrayList<>();
            do {
                FrameSysmexXS500i frame = readFrame();
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
            mess = new MessageSysmexXS500i(records);
        } catch (Exception ex) {
            throw new SysmexXS500iException(ex);
        }
        return mess;
    }

    @Override
    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Result> getResultsFromMessage(Object messageObj) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        RecordProducer recordProducer = new RecordProducerSysmexXS500i();
        try {
            MessageSysmexXS500i message = (MessageSysmexXS500i) messageObj;
            List<Record> listRecords = message.getRecords();
            String sid = "";
            for (Record record : listRecords) {
                if (record.getTypeRecord() == TypeRecord.ORDER) {
                    sid = recordProducer.getSidFromOrderRecord(record);
                } else if (record.getTypeRecord() == TypeRecord.RESULT) {
                    RecordResultSysmexXS500i recordResult = (RecordResultSysmexXS500i) recordProducer.getResultRecord(record);
                    Result result = new Result();
                    result.setSid(sid);
                    result.setTestCode(recordResult.getTestCode());
                    result.setInstrument(instrument.getName());
                    String addParams = "";
                    result.setValue(recordResult.getValue());
                    result.setUnits(recordResult.getUnits());
                    result.setReferenseRanges(recordResult.getRefRanges());
                    result.setAbnormalFlags(recordResult.getAbnormalFlags());
                    addParams = "natureAbnormalityTesting" + "=" + recordResult.getNatureAbnormalityTesting();
                    addParams += ",resultStatus" + "=" + recordResult.getResultStatus();
                    result.setAddParams(addParams);

                    int parameterId = 0;
                    Test test = model.getTestByTestCode(result.getTestCode(), this.instrument.getId());
                    if (test != null) {
                        parameterId = test.getParameter().getId();
                    } else {
                        log("info", "unknown test code: " + result.getTestCode(), true);
                    }
                    result.setParameterId(parameterId);
//                    WorkOrder wo = model.getWorkOrderBySidAndInstrument(result.getSid(), this.instrument.getId());
//                    if (wo != null) {
//                        result.setWorkOrderId(wo.getId());
//                    }
                    int lastVersion = model.getVersionOfLastResultByInstrumentAndSid(instrument.getName(), sid, result.getTestCode());
                    result.setVersion(lastVersion + 1);
                    list.add(result);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SysmexXS500iException(ex);
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
            throw new SysmexXS500iException(ex);
        }
    }

    /**
     *
     * Отправка тестового ENQ для ASTM протоколов - (Посылаем ENQ, получаем ACK,
     * посылаем EOT)
     */
    protected void sendTestASTMEnq() throws IOException, BreakException {
        outputStream.write(ENQ);
        threadSleep(5000);
        if (inputStream.available() == 0) {
            logTemp("info", "no answer after ENQ sending", true);
            return;
        }
        byte inputValueMainRun = (byte) inputStream.read();
        if (inputValueMainRun == ACK) {
            logTemp("info", "ENQ-ACK is OK", true);
        }
    }

    public FrameSysmexXS500i readFrame() throws SysmexXS500iException {

        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = ASCII.STX;
        byte[] newByteArray;
        FrameSysmexXS500i frame;
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
                    newByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                    frame = new FrameSysmexXS500i(new String(newByteArray));
                    break;
                }
            } catch (IOException ex) {
                throw new SysmexXS500iException(ex);
            }
        }
        return frame;
    }
}
