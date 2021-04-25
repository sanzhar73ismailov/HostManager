package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.Test;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.io.IOException;
import java.util.List;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.DimensionErrorCode;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.DimensionErrorCodes;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.DimensionXpandException;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.MessageDimensionXpand;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.ProduceMessage;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.ResultDimensionXpand;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.ResultMessageDimensionXpand;
import kz.biostat.lishostmanager.comport.instrument.dimensionXpand.TypeReceivedMessage;
import java.util.ArrayList;
import java.util.Map;

import kz.biostat.lishostmanager.comport.modelHost.Model;

public class DriverDimensionXpand extends DriverInstrument {

    byte inputValueMainRun;
    MessageDimensionXpand inputMessage;
    MessageDimensionXpand outputMessage;
    int counter = 0;
    int counterAnswer = 0;
    ProduceMessage produceMessage = new ProduceMessage();
    private int COMMUNOCATION_MODE = SEND_RECEIVE;
    private static int SEND_ONLY = 1;
    private static int SEND_RECEIVE = 2;
    private static int SEND_ID_RECEIVE = 3;
    Map<Integer, DimensionErrorCode> mapErrors = DimensionErrorCodes.getMapErrorCodes();
    private boolean receivePollWithoutPreviousACK = false;

    public DriverDimensionXpand(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws BreakException, InstrumentException, IOException {
        WorkOrder wo = null;
        if (isStoppedMashine()) {
            throw new BreakException("mashine " + instrument.getName() + " stopped");
        }
        counter++;
        try {
            //для тех случаев, когда POLL посылается сразу после получени NoReQuest message (см. sendNoRequestMessage() )
            if (!receivePollWithoutPreviousACK) {
                threadSleep(12000);
                if (inputStream.available() == 0) {
                    //через каждые полчаса
                    if (testMode && ((counter == 1) || (counter % 150 == 0))) {
                        //logTemp("info", "Idle status..., counter: " + counter, true);
                    }
                    return;
                }
                inputValueMainRun = (byte) inputStream.read();
            }else{
                receivePollWithoutPreviousACK = false;
            }
            if (inputValueMainRun == STX) {
                inputMessage = getMessage();
                //log(TO_HOST, inputMessage.getMessageAsString(), true);
                TypeReceivedMessage typeReceivedMessage = inputMessage.getTypeReceivedMessage();
                if (!inputMessage.validateCheckSum()) {
                    outputStreamWrite(NACK);
                    throw new ContinueException("check sum not validated");
                } else {
                    outputStream.write(ACK);
                    if (typeReceivedMessage != TypeReceivedMessage.POLL_MESSAGE) {
                        log(TO_INSTRUMENT, ASCII.getASCIICodeAsString(ACK), true);
                    }
                }

                switch (typeReceivedMessage) {
                    case POLL_MESSAGE:
                        if (inputMessage.isBusy() && testMode) {
                            logTemp("info", "Poll message. Is first:"
                                    + inputMessage.isFirstPoll() + ", is free: "
                                    + !inputMessage.isBusy() + ". Details:", true);
                        } else {
                            if (instrument.getMode() == Instrument.ModeWorking.BATCH) {
                                if (existOrders()) {
                                    logTemp(TO_HOST, inputMessage.getMessageAsString(), true);
                                    if (testMode) {
                                        logTemp("info", "There exist workorders to send", true);
                                    }
                                    sendWorkOrdersBatch();
                                } else {
                                    sendNoRequestMessage();
//                                    outputMessage = produceMessage.noRequestMessage();
//                                    outputStreamWrite(outputMessage.getRawMessage());
//                                    inputValueMainRun = (byte) inputStream.read();
//                                    if (inputValueMainRun != ACK) {
//                                        logTemp("info", "Poll->NoRequest->Not ACK received", true);
//                                    } else {
//                                        if (testMode && ((counter == 1) || (counter % 150 == 0))) {
//                                            logTemp("info", "Poll->NoRequest->ACK is OK", true);
//                                        }
//                                    }
                                }
                            } else {
                                sendNoRequestMessage();
                            }
                        }
                        break;
                    case QUERY_MESSAGE:
                        String sid = inputMessage.getSidFromQueryRecord();
                        log(TO_HOST, "Query message for sid " + sid + ": " + inputMessage.getMessageAsString(), true);
                        wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());
                        if (wo == null) {
                            sendNoRequestMessage();
                        } else {
                            if (sendWorkOrder(wo, false)) {
                                model.setWorkOrderAsServed(wo.getId());
                            } else {
                                throw new ContinueException("WorkOrder was not send: " + wo);
                            }
                        }
                        break;
                    case REQUUEST_ACCEPTANCE_MESSAGE:
                        log(TO_HOST, inputMessage.getMessageAsString(), true);
                        if (inputMessage.isWorkorderAccepted()) {
                            model.setWorkOrderAsServed(wo.getId());
                            wo = null;
                        } else {
                            throw new ContinueException("Workorders is not accepted, reason: "
                                    + inputMessage.getDiscriptionOfReasonOfNotAccepted());
                        }
                        break;
                    case RESULT_MESSAGE:
                        log(TO_HOST, inputMessage.getMessageAsString(), true);
                        if (testMode) {
                            logTemp(TO_HOST, "Result message: " + inputMessage.getMessageAsString(), true);
                        }
                        saveResults(inputMessage);
                        break;
                    case CALIBRATION_RESULT_MESSAGE:
                        log(TO_HOST, inputMessage.getMessageAsString(), true);
                        if (testMode) {
                            logTemp(TO_HOST, "calibration message: " + inputMessage.getMessageAsString(), true);
                            outputStreamWrite(produceMessage.resultAcceptanceMessage().getRawMessage());
                        }
                        inputValueMainRun = inputStreamRead();
                        break;
                    default:
                        log(TO_HOST, "Unknown message: " + inputMessage.getMessageAsString(), true);
                        break;
                }
            } else {
                throw new ContinueException("not STX symbole: " + ASCII.getASCIICodeAsString(inputValueMainRun));
            }
        } catch (ContinueException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DimensionXpandException(ex);

        }
    }

    @Override
    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        try {
            ProduceMessage pm = new ProduceMessage();
            MessageDimensionXpand mess = pm.sampleRequestMessage(workOrder);
            outputStreamWrite(mess.getRawMessage());
            inputValueMainRun = inputStreamRead();
            if (inputValueMainRun == ACK) {
                inputValueMainRun = inputStreamRead();
                if (inputValueMainRun == STX) {
                    inputMessage = getMessage();
                    log(TO_HOST, inputMessage.getMessageAsString(), true);
                    TypeReceivedMessage typeReceivedMessage = inputMessage.getTypeReceivedMessage();
                    outputStreamWrite(ACK);
                    logTemp("info", "inputMessage.isWorkorderAccepted() = " + inputMessage.isWorkorderAccepted(), true);
                    if (typeReceivedMessage == TypeReceivedMessage.REQUUEST_ACCEPTANCE_MESSAGE
                            && inputMessage.isWorkorderAccepted()) {
                        return true;
                    } else {
                        log("<b>info</b>", "Workorders is not accepted, reason:"
                                + inputMessage.getDiscriptionOfReasonOfNotAccepted(), true);
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (inputValueMainRun == STX) {
                inputMessage = getMessage();
                log(TO_HOST, inputMessage.getMessageAsString(), true);
                outputStreamWrite(ACK);
                return false;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new DimensionXpandException(ex);
        }
    }

    @Override
    public List<Result> getResultsFromMessage(Object message) throws InstrumentException {
        List<Result> results = new ArrayList<>();
        try {
            ResultMessageDimensionXpand messageFromInstrument = new ResultMessageDimensionXpand(((MessageDimensionXpand) message).getRawMessage());
            List<ResultDimensionXpand> list = messageFromInstrument.getListResults();

            for (ResultDimensionXpand resultDimensionXpand : list) {
//                 logTemInfo("resultDimensionXpand = " + resultDimensionXpand);

                if (!resultDimensionXpand.getErrorCode().isEmpty()) {
                    //logTemInfo("Error code 9: " + resultDimensionXpand);
                    DimensionErrorCode errorObj = mapErrors.get(Integer.parseInt(resultDimensionXpand.getErrorCode()));
                    //если код ошибки - 9 или другой, который подавляет результаты, значит реагента нет, пропускаем
                    if (errorObj.isSuppressResult()) {
                        logTemp("info", "Saving but there are SuppressResult!!! error for " + resultDimensionXpand + ", error is " + errorObj, true);
                        //continue;
                    } else {
                        logTemp("info", "Saving but there are error for " + resultDimensionXpand + ", error is" + errorObj, true);
                    }
                }
                //если рузультат пустой тоже пропускаем
                if (resultDimensionXpand.getValue() != null && resultDimensionXpand.getValue().trim().isEmpty()) {
                    //logTemInfo("Error code 9: " + resultDimensionXpand);
                    logTemp("info", "Result is empty (will not save): " + resultDimensionXpand, true);
                    continue;
                }
                Result result = new Result();
                String testCode = resultDimensionXpand.getTestName();
                String sid = messageFromInstrument.getSid();
                result.setSid(sid);
                result.setTestCode(testCode);
                result.setInstrument(instrument.getName());

                result.setValue(resultDimensionXpand.getValue());
                result.setUnits(resultDimensionXpand.getUnits());
                String addParams = "";
                //result.setReferenseRanges(recordResult.getRefRanges());
                result.setAbnormalFlags(resultDimensionXpand.getErrorCode());
                // addParams = "natureAbnormalityTesting" + "=" + recordResult.getNatureAbnormalityTesting();
                //  addParams += ",resultStatus" + "=" + recordResult.getResultStatus();
                result.setAddParams(addParams);

                Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                if (test != null) {
                    result.setParameterId(test.getParameter().getId());
                    WorkOrder wo = model.getWorkOrderBySidAndInstrument(sid, this.instrument.getId());
                    if (wo != null) {
                        result.setWorkOrderId(wo.getId());
                    }
                    int lastVersion = model.getVersionOfLastResultByInstrumentAndSid(instrument.getName(), sid, testCode);
                    result.setVersion(lastVersion + 1);
                    results.add(result);
                } else {
                    log("info", "No parameter for testCode: " + testCode, true);
                }
            }
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
        return results;
    }

    private MessageDimensionXpand getMessage() throws DimensionXpandException {
        MessageDimensionXpand message = null;
        int indexOfByteArray = 0;
        int sizeArray = 10000;
        byte[] byteArray = new byte[sizeArray];
        byteArray[indexOfByteArray++] = ASCII.STX;
        byte[] neewByteArray;

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
                    neewByteArray = java.util.Arrays.copyOf(byteArray, indexOfByteArray);
                    message = new MessageDimensionXpand(neewByteArray);
                    break;
                }
            } catch (IOException ex) {
                throw new DimensionXpandException(ex);
            }
        }
        return message;
    }

    @Override
    public void saveResults(Object messageInput) throws InstrumentException {
        try {
            MessageDimensionXpand messageInputDimensionXPand = (MessageDimensionXpand) messageInput;
            byte[] byteArray = messageInputDimensionXPand.getRawMessage();
            ResultMessageDimensionXpand resMessage = new ResultMessageDimensionXpand(byteArray);
            //List<ResultDimensionXpand> list = resMessage.getListResults();
            List<Result> results = getResultsFromMessage(resMessage);
            boolean resultSaved = false;
            for (Result result : results) {
                resultSaved = model.insertObject(result) > 0;
                if (!resultSaved) {
                    log("info", "result not saved: " + result, true);
                    break;
                }
            }
            if (COMMUNOCATION_MODE != SEND_ONLY) {
                MessageDimensionXpand resultAcceptanceOrRejectedMessage = null;
                // если результатов нет или все сохранены, посылаем подтверждение получения результатов
                if (results.size() == 0 || resultSaved) {
                    resultAcceptanceOrRejectedMessage = produceMessage.resultAcceptanceMessage();
                } else {
                    resultAcceptanceOrRejectedMessage = produceMessage.resultRejectedMessage();
                }
                outputStreamWrite(resultAcceptanceOrRejectedMessage.getRawMessage());
                inputValueMainRun = inputStreamRead();
            }
        } catch (Exception ex) {
            throw new ContinueException(ex);
        }
    }

    @Override
    public void sendWorkOrdersBatch() throws InstrumentException {
        try {
            List<WorkOrder> orderList = model.getWorkOrdersByInstrumentWithFeeStatus(this.instrument.getId());
            if (orderList.size() > 0) {
                if (testMode) {
                    logTemp("info", String.format("There has %s workorders to send for " + instrument.getName(), orderList.size()), true);
                }
            } else {
                if (testMode) {
                    logTemp("info", "There no workorders to send for " + instrument.getName(), true);
                }
            }
            for (int i = 0; i < orderList.size(); i++) {
                WorkOrder workOrder = orderList.get(i);
                if (sendWorkOrderBatch(workOrder)) {
                    model.setWorkOrderAsServed(workOrder.getId());
                    break;
                } else {
                    //если даже не принял, помечаем его выполненным, чтобы работа не стояла
                    model.setWorkOrderAsServed(workOrder.getId());
                    log("info", "workOrder: " + workOrder + ", is not accepted", true);
                    break;
                }
            }
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
    }

    private void sendNoRequestMessage() throws IOException {
        outputMessage = produceMessage.noRequestMessage();
        outputStream.write(outputMessage.getRawMessage());
        inputValueMainRun = (byte) inputStream.read();
        counterAnswer++;
        if (inputValueMainRun != ACK) {
            logTemp("info", "Poll->NoRequest->Not ACK received: "
                    + ASCII.getASCIICodeAsString((byte) inputValueMainRun), true);
            receivePollWithoutPreviousACK = true;
        } else {
            if ((counterAnswer == 1) || (counterAnswer % 150 == 0)) {
                logTemp("info", "Poll->NoRequest->ACK is OK", true);
            }
        }
    }

}
