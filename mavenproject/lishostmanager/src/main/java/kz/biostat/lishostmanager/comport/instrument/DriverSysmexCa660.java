/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.SysmexCA660Message;
import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.BlockSysmexCA660;
import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.Data;
import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.SysmexCa660Exception;
import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.TypeMessageSysmexCa660;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kz.biostat.lishostmanager.comport.modelHost.Model;

/**
 *
 * @author sanzhar.ismailov
 */
public class DriverSysmexCa660 extends DriverInstrument {

    long counter = 0L;
    private byte inputValueMainRun;
    SysmexCA660Message messageInput;
    //Message messageOut;

    public DriverSysmexCa660(Instrument instrument, Model model) {
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
                TypeMessageSysmexCa660 type = messageInput.getTypeOfMessage();
                switch (type) {
                    case QUERY:
                        String sid = messageInput.getSidFromQueryRecord();
                        WorkOrder wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());
                        if (testMode) {
                            //logTemp("info", "wo = " + wo, true); //todo comment after
                        }
                        if (wo != null) {
                            if (sendWorkOrder(wo, false)) {
                                model.setWorkOrderAsServed(wo.getId());
                            }
                        } else {
                            logTemp("info", "there no wo with sid: " + sid, true); //todo comment after
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

    public List<BlockSysmexCA660> readBlocks() throws IOException, InstrumentException {
        List<BlockSysmexCA660> listBlocks = new ArrayList<>();
        BlockSysmexCA660 block;
        do {
            block = readBlock();
            log(TO_HOST, block.getBlockAsString(), true);
            if (testMode) {
                logTemInfo("block = " + block.getBlockAsString());
            }
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

    public BlockSysmexCA660 readBlock() throws IOException {
        BlockSysmexCA660 block = null;
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
                block = new BlockSysmexCA660(new String(neewByteArray));
                break;
            }
        }
        return block;
    }

    public SysmexCA660Message readMessage() throws IOException, InstrumentException {
        List<BlockSysmexCA660> readBlock = readBlocks();
        SysmexCA660Message message = new SysmexCA660Message(readBlock);
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
            throw new SysmexCa660Exception(ex);
        }
    }

    @Override
    public List<Result> getResultsFromMessage(Object message) throws InstrumentException {
        List<Result> list = new ArrayList<>();
        SysmexCA660Message inputMessage = (SysmexCA660Message) message;
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
                Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                if (test == null) {
                    logTemInfo("testCode: " + testCode + " unknown");
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
            throw new SysmexCa660Exception(ex);
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
            throw new SysmexCa660Exception(ex);
        }
    }

}
