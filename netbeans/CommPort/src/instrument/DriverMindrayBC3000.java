package instrument;

import entity.Instrument;
import entity.Result;
import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.mindrayBC3000.Data;
import instrument.mindrayBC3000.MessageMindrayBC3000;
import instrument.mindrayBC3000.Parameter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import modelHost.Model;

public class DriverMindrayBC3000 extends DriverInstrument {

    private byte inputValueMainRun;
//    private int inputValueMainRun;
    //private int inputValueMainRun;
    int counter = 0;

    public DriverMindrayBC3000(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws InstrumentException, IOException {
        counter++;
        if (isStoppedMashine()) {
            throw new BreakException("mashine " + instrument.getName() + " stopped");
        }
        try {
            threadSleep(3000);
            if (inputStream.available() == 0) {
                /* Записываем номер цикла каждые 30 мин. */
                if ((counter == 1) || (counter % 600 == 0)) {
                    logTemp("info", "cycle: " + counter, true);
                }
                return;
            }
            inputValueMainRun = (byte) inputStream.read();
            if (inputValueMainRun == STX) {
                MessageMindrayBC3000 message = new MessageMindrayBC3000(readMessage());
                log(TO_HOST, message.getMessageAsString(), true);
                String type = message.getType();
                if (type == null || type.trim().isEmpty()) {
                    type = "Unknown";
                }
                switch (type) {
                    case "A":
                        logTemp(TO_HOST, "результаты", true);
                        //logTemInfo("typeMessage: RESULT_FROM_INSTRUMENT");
                        saveResults(message);
                        break;
                    case "B":
                        logTemp(TO_HOST, "Standard L-J QC Data Format", true);
                        break;
                    case "C":
                        logTemp(TO_HOST, "Run L-J QC Data Format", true);
                        break;
                    default:
                }

            } else {
                logTemp("info", "Other than STX: " + getASCIICodeAsString(inputValueMainRun)
                        + ", number: " + inputValueMainRun,
                        true);
            }

        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
    }

    @Override
    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Result> getResultsFromMessage(Object message) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        MessageMindrayBC3000 messageMindray = (MessageMindrayBC3000) message;
        List<Data> listData = messageMindray.getParameter().dataList;
        try {
            StringBuffer stbForUnknownTests = new StringBuffer();
            for (Data dataElem : listData) {
                //logTemInfo("data = <<<" + dataElem.getValue() + ">>>");
                Result result = new Result();
                String testCode = dataElem.getTestCode();
                result.setTestCode(testCode);
                String sid = messageMindray.getSid().trim();
                result.setSid(sid);
                result.setInstrument(instrument.getName());
                result.setValue(dataElem.getValue());
                String units = dataElem.getUnit();
                if (units != null && units.length() > 20) {
                    units = units.substring(0, 20);
                }
                result.setUnits(units);
                entity.Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                if (test == null) {
                    stbForUnknownTests.append(testCode).append("=").append(dataElem.getValue()).append(", ");
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
            logTemp("info", "unknown testCodes (texsCode=value format): " + stbForUnknownTests.toString(), true);
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
        return list;
    }

    @Override
    public void saveResults(Object inputMessage) throws InstrumentException {
        MessageMindrayBC3000 messageMindray = (MessageMindrayBC3000) inputMessage;
        try {
            List<Result> listResults = getResultsFromMessage(inputMessage);
            for (Result result : listResults) {
                model.insertObject(result);
            }
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }

//        logTemInfo("<<<<<<<<<<<<text=" + text + ">>>>>>>>>>>>>>");
    }

    public byte[] readMessage() throws IOException {
        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = inputValueMainRun;
        byte[] neewByteArray;

        while (true) {
            inputValueMainRun = (byte) inputStream.read();
            if (sizeArray == indexOfByteArray) {
                sizeArray *= 2;
                byte[] tempArray = new byte[sizeArray];
                System.arraycopy(byteArray, 0, tempArray, 0, byteArray.length);
                byteArray = tempArray;
            }
            if (inputValueMainRun != EOF) {
                byteArray[indexOfByteArray++] = inputValueMainRun;
            } else {
                //logTemInfo("ETX = " + ASCII.getASCIICodeAsString(inputValueMainRun));
                byteArray[indexOfByteArray++] = inputValueMainRun;
                neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                break;

            }
        }
        return neewByteArray;
    }

}
