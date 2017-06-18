package instrument;

import entity.Instrument;
import entity.Result;
import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.sysmexCA1500.BlockSysmexCA1500;
import instrument.sysmexCA1500.Data;
import instrument.sysmexCA1500.SysmexCA1500Message;
import instrument.sysmexCA1500.SysmexCa1500Exception;
import instrument.sysmexCA1500.TypeMessageSysmexCa1500;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelHost.Model;

public class DriverSysmexCa1500 extends DriverInstrument {

    long counter = 0L;
    private byte inputValueMainRun;
    SysmexCA1500Message messageInput;
    //Message messageOut;

    public DriverSysmexCa1500(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws InstrumentException, IOException {
        if (isStoppedMashine()) {
            throw new BreakException("mashine " + instrument.getName() + " stopped");
        }
        try {
            threadSleep(5000);
            counter++;
            if (inputStream.available() == 0) {

                /* Записываем номер цикла каждые 30 мин. */
                if ((counter == 1) || (counter % 360 == 0)) {
                    logTemp("info", "cycle: " + counter, true);
                }
                return;
            }
            //inputValueMainRun = (byte) inputStream.read();
            inputValueMainRun = inputStreamRead();
            if (inputValueMainRun == STX) {
                this.messageInput = readMessage();
                if (testMode) {
                    logTemp("info", messageInput.toString(), true);
                }
                TypeMessageSysmexCa1500 type = messageInput.getTypeOfMessage();
                switch (type) {
                    case QUERY:
                        WorkOrder wo = null;
                        String sid = messageInput.getSidFromQueryRecord();
                        if (sid != null && !sid.trim().isEmpty()) {
                            // если длина sid меньше 3, значит он типа 1 или 2 или 11, максимум 999
                            // значит работа ведется без баркода, поэтому добавляем дату через дефис
                            //чтобы получить задания из базы
                            if (sid.length() < 3) {
                                sid = sid + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
                            }
                            wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());
                        }
                        if (testMode) {
                            //logTemp("info", "wo = " + wo, true); //todo comment after
                        }
                        if (wo != null) {
                            if (sendWorkOrder(wo, false)) {
                                model.setWorkOrderAsServed(wo.getId());
                            }
                        } else {
                            logTemp("info", "there no wo with sid: " + sid
                                    + ", rack " + messageInput.getRackNumber()
                                    + ", position " + messageInput.getPositionTubeNumber(), true); //todo comment after
                            String orderEmptyFromQueryMessage = messageInput.createOrderEmptyFromQueryMessage();
                            outputStreamWrite(orderEmptyFromQueryMessage.getBytes());
                            inputValueMainRun = inputStreamRead();
                        }
                        break;
                    case RESULT:
                        saveResults(messageInput);
                        break;
                }

            } else {
                logTemp("info", "Other than STX: " + getASCIICodeAsString(inputValueMainRun)
                        + ", int val=" + inputValueMainRun, true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InstrumentException(ex);
        }
    }

    public List<BlockSysmexCA1500> readBlocks() throws IOException, InstrumentException {
        List<BlockSysmexCA1500> listBlocks = new ArrayList<>();
        BlockSysmexCA1500 block;
        do {
            block = readBlock();
            log(TO_HOST, "block number " + block.getNumber()
                    + ", from " + block.getTotalNumbers() + " numbers. Text: " + block.getBlockAsString(), true);
            outputStreamWrite(ACK);
            listBlocks.add(block);
            if (!block.isFinalBlock()) {
                inputValueMainRun = inputStreamRead();
                if (inputValueMainRun != STX) {
                    throw new InstrumentException("it's not STX : " + getASCIICodeAsString(inputValueMainRun));
                }
            }
        } while (!block.isFinalBlock());
        return listBlocks;
    }

    public BlockSysmexCA1500 readBlock() throws IOException {
        BlockSysmexCA1500 block = null;
        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = ASCII.STX;
        byte[] neewByteArray;

        while (true) {
            byte incomeValue = (byte) inputStream.read();
            if (sizeArray == indexOfByteArray) {
                sizeArray *= 2;
                byte[] tempArray = new byte[sizeArray];
                System.arraycopy(byteArray, 0, tempArray, 0, byteArray.length);
                byteArray = tempArray;
            }
            if (incomeValue != ETX) {
                byteArray[indexOfByteArray++] = incomeValue;
            } else {
                byteArray[indexOfByteArray++] = incomeValue;
                neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                block = new BlockSysmexCA1500(new String(neewByteArray));
                break;
            }
        }
        return block;
    }

    public SysmexCA1500Message readMessage() throws IOException, InstrumentException {
        List<BlockSysmexCA1500> readBlock = readBlocks();
        SysmexCA1500Message message = new SysmexCA1500Message(readBlock);
        return message;
    }

    @Override
    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        try {
//            String str = SysmexCA660Message.createOrderFromWorkOrder(workOrder);
            String str = this.messageInput.createOrderFromWorkOrderFromThis(workOrder);
            outputStreamWrite(str.getBytes());
            inputValueMainRun = inputStreamRead();
            return inputValueMainRun == ACK;
        } catch (Exception ex) {
            throw new SysmexCa1500Exception(ex);
        }
    }

    @Override
    public List<Result> getResultsFromMessage(Object message) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        SysmexCA1500Message inputMessage = (SysmexCA1500Message) message;
        List<Data> listData = inputMessage.getData();
        try {
            for (Data dataElem : listData) {
                //logTemInfo("data = <<<" + dataElem.getValue() + ">>>");
                if (dataElem.getValue() == null || dataElem.getValue().trim().equals("----")) {
                    continue;
                }
                Result result = new Result();
                String testCode = dataElem.getTestCodeForResult();
                result.setTestCode(testCode);
                String sid = inputMessage.getSid().trim();
                result.setSid(sid);
                result.setInstrument(instrument.getName());
                result.setValue(dataElem.getValue());
                String units = dataElem.getUnits();
                if (units != null && units.length() > 20) {
                    units = units.substring(0, 20);
                }
                result.setUnits(units);
                result.setAbnormalFlags(dataElem.getFlag());
                entity.Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                if (test == null) {
                    log("info", "unknown testCode: " + testCode, true);
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
        } catch (Exception ex) {
            throw new SysmexCa1500Exception(ex);
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
            throw new SysmexCa1500Exception(ex);
        }
    }

}
