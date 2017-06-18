package instrument;

import entity.*;
import instrument.advia2120.*;
import static instrument.ASCII.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelHost.*;

public class DriverAdvia2120 extends DriverInstrument {

    int stage = STAGE_INITIALIZATION;
    int status = STATUS_MASTER;
    int attempt = 1;
    Advia2120Message messageInput;
    Advia2120Message messageOut;
    private static final int STAGE_INITIALIZATION = 1;
    private static final int STAGE_DOWNLOADING_WORKORDER = 2;
//    public static final int STAGE_TOKEN_TRANSFER = 3;
    private static final int STATUS_MASTER = 1;
    private static final int STATUS_SLAVE = 0;
    /* TLS - время в млс между получением сообщения и отправкой МТ (подтверждения)   */
    private static final int TLS = 25;
    /* TS - время в млс между отправкой последнего МТ (подтверждения) и отправкой Token Transfer (S) сообщения   */
    private static final int TS = 5000;
    /* WATCH_DOG - время в млс когда нужно делать реинициализацию, если нет никакого ответа от аппарата   */
    private static final int WATCH_DOG = 20000;
    /* mode - режим работы аппарата*/
    /* промежуток времени (mls) через который будет проводоиться повторная инициализация, если ответа 
     нет через например TIMES_TO_GO_SLOW_INIT_REGIME неудачных попыток
     */
    private static int MLS_IF_INIT_NO_ANSWER = 30 * 60 * 1000;
    //public static int MLS_IF_INIT_NO_ANSWER = 20 * 1000;
    /*количество раз, после которых инициализацию проводим реже (см. параметр TIME_IF_INIT_NO_ANSWER)*/
    private final static int TIMES_TO_GO_SLOW_INIT_REGIME = 5;
    private static final int DOWNLOADNING_MODE = 1;
    private static final int QUERY_MODE = 2;
    private static final int workOrderMode = QUERY_MODE;

    public DriverAdvia2120(Instrument instrument, Model model) {
        super(instrument, model);
    }

    @Override
    public void driverRun() throws InstrumentException, IOException {
        byte incomeValue;
        long bigCounter = 0L;
        try {

            if (isStoppedMashine()) {
                throw new BreakException("mashine " + instrument.getName() + " stopped");
            }
            if (status == STATUS_MASTER) {
                switch (stage) {
                    case STAGE_INITIALIZATION:
                        if (!init()) {
                            if (attempt > TIMES_TO_GO_SLOW_INIT_REGIME) {
                                if (attempt == (TIMES_TO_GO_SLOW_INIT_REGIME + 1)) {
                                    //после TIMES_TO_GO_SLOW_INIT_REGIME попыток делаем инициализацию каждые 10 минут (удлиняем промужуток)
                                    log("info", "GO TO REAR INIT, EVERY " + MLS_IF_INIT_NO_ANSWER / 1000 / 60 + " min", true);
                                }
                                threadSleep(MLS_IF_INIT_NO_ANSWER);
                                //threadSleep(1*60 * 1000);
                            }
                            return;
                            //throw new ContinueException("Init again");
                        } else {
                            this.attempt = 1;
                            // если есть задания то переходим в стадию отправки заданий
                            // в ином случае посылаем TokenTransfer сообщение, чтобы
                            // перейти в режим Slave для получения результатов
                            if (instrument.getMode() == Instrument.ModeWorking.BATCH) {
                                //stage = STAGE_DOWNLOADING_WORKORDER;
                                //status = STATUS_MASTER;
                                //status = STATUS_SLAVE;
                                sendTokenTransfer(messageOut);
                                return;
                            } else {
                                if (!sendTokenTransfer(messageOut)) {
                                    throw new ContinueException("sending token transfer failed");
                                } else {
                                    status = STATUS_SLAVE;
                                }
                            }
                        }
                        break;
                    case STAGE_DOWNLOADING_WORKORDER:
                        logTemInfo("Check if exist workorders to send");
                        attempt = 1;
                        sendWorkOrdersBatch();
                        break;
                    // case STAGE_TOKEN_TRANSFER:
                    //   sendTokenTransfer(false);
                    // break;
                    default:
                        throw new Advia2120Exception("Unknown stage " + stage);
                }
            } else if (status == STATUS_SLAVE) {
                this.threadSleep(TS + 5000);
                if (isStoppedMashine()) {
                    throw new BreakException("mashine stopped");
                }
                if (inputStream.available() == 0) {
                    status = STATUS_MASTER;
                    stage = STAGE_INITIALIZATION;
                    throw new ContinueException("No answer for Advia2120. Go to INIT");
                }

                incomeValue = (byte) inputStream.read();

                if (incomeValue != STX) {
                    log(TO_HOST, ASCII.getASCIICodeAsString(incomeValue), true);
                    //throw new ContinueException("Received NOT <STX>, incomeValue = " + incomeValue);
                    log("", "Received NOT <STX>, incomeValue = " + incomeValue, true);
                    if (this.attempt < 3) {
                        outputStream.write(NACK);
                        this.attempt++;
                        log(TO_INSTRUMENT, "<NACK>", true);
                    } else {
                        status = STATUS_MASTER;
                        stage = STAGE_INITIALIZATION;
                    }
                    return;
                } else {
                    messageInput = new Advia2120Message(readMessage(inputStream, incomeValue));

                    String typeMessage = messageInput.getType();
                    switch (typeMessage) {
                        case "R":
                            log(TO_HOST, messageInput.getMessageAsString(), true);
                            if (1 == 0) {
                                throw new BreakException("test BreakException для тестирования, чтобы не получать результаты");
                            } else {
                                saveResults(messageInput);
                            }
                            break;
                        case "S":
                            bigCounter++;
                            if (testMode) {
                                logTemp(TO_HOST, messageInput.getMessageAsString(), true);
                            }

                            if (!messageInput.validateCheckSum()) {
                                sendNack();
                            } else {
                                threadSleep(TLS);
                                outputStream.write(messageInput.getMessageToggle());
                                if (testMode) {
                                    logMtToInstrumentTemp(messageInput.getMessageToggle());
                                }
                                threadSleep(TS);
                                attempt = 1;
                                if (1 == 0) {
                                    sendWorkOrdersBatch();
                                }
                                //if (1 == 0) {
                                sendTokenTransfer(messageInput);
                                if (bigCounter % 50 == 0) {
                                    //Так как аппарат будет работать в однонаправленном
                                    //режиме, то задания на него не отправляются,
                                    // а просто помечаются как обслуженные
                                    markWorkOrdersAsServed();
                                }
                                //}
                            }
                            break;
                        case "Q":
                            log(TO_HOST, messageInput.getMessageAsString(), true);
                            if (testMode) {
                                logTemInfo("H<-M. query: " + messageInput.getMessageAsString());
                            }
                            if (!messageInput.validateCheckSum()) {
                                sendNack();
                            } else {
                                threadSleep(TLS);
                                outputStream.write(messageInput.getMessageToggle());
                                logMtToInstrument(messageInput.getMessageToggle());
                                threadSleep(TS);
                                attempt = 1;
                                String sid = messageInput.getSidFromQueryMessage(); // получаем sid
                                WorkOrder wo = model.getWorkOrderBySidAndInstrument(sid, instrument.getId());
                                if (wo != null) {
                                    sendWorkOrderReply(wo);
                                } else {
                                    sendNoWorkOrder(sid);
                                }
                            }
                            break;
                        default:
                            log(TO_HOST, messageInput.getMessageAsString(), true);
                            throw new Advia2120Exception("H<-M from server: MESSAGE BUT UNKNOWN " + messageInput.getMessageAsString());
                    }
                }
            } else {
                throw new InstrumentException("Wrong status: " + status);
            }

        } catch (InterruptedException | ModelException e) {
            log("exception", e.getMessage(), true);
            e.printStackTrace();
            throw new InstrumentException(e);
        }

    }

    /**
     * Для очистки входящего буфер для инициализации
     */
    private String cleanedBuffer(int available) throws IOException {
        byte[] buffer = new byte[available];
        for (int i = 0; i < available; i++) {
            buffer[i] = (byte) inputStream.read();
        }
        return ASCII.getStringFromByteArray(buffer);
    }

    private boolean init() throws IOException, MtIsNotInDiapazonException, BreakException {
        /*
         считываем - если есть данные из входящего буфера, чтобы начать инициализацию с чистого листа 
         Очищаем входящий буфер для инициализации 
         */
        int available = inputStream.available();
        if (available > 0) {
            logTemp(TO_HOST, "cleaned buffer:" + cleanedBuffer(available), true);
        }

        byte incomeValue = 0;
        messageOut = new Advia2120Message(Advia2120Message.MESSAGE_INIT_NO_IMAGES);
        String str = "INIT, attempt " + attempt + ", ThreadID: " + Thread.currentThread().getId() + ", " + messageOut.getMessageAsString();
        outputStream.write(messageOut.getRawMessage());
        if (attempt == 1) {
            log(TO_INSTRUMENT, str, true);
        } else {
            logTemp(TO_INSTRUMENT, str, true);
        }
        attempt++;

        threadSleep(5000); // здесь происходит java.lang.InterruptedException: sleep interrupted
        //threadSleep(1000); // for testing

        available = inputStream.available();
        if (available == 0) {
            if (testMode) {
                logTemInfo("No answer from apparatus");
            }
            threadSleep(WATCH_DOG);
            // threadSleep(500);  // for testing
            return false;
        }
        incomeValue = inputStreamReadLogTemp();
        if (incomeValue == NACK) {
            logTemInfo("H<-M. Received NACK. Init again");
            threadSleep(TS);
            return false;
        } else if (incomeValue != messageOut.getMessageToggle()) {
            logTemInfo("H<-M. Received wrong MT: " + incomeValue + ". Init again");
            threadSleep(TS);
            return false;
        } else {
            this.attempt = 1;
            return true;
        }

    }

    public void markWorkOrdersAsServed() throws InstrumentException {
        try {
            List<WorkOrder> orderList = model.getWorkOrdersByInstrumentWithFeeStatus(this.instrument.getId());
            if (orderList.size() > 0) {
                logTemInfo(String.format("There has %s workorders to send for " + instrument.getName(), orderList.size()));
            } else {
                logTemInfo("There no workorders to send for " + instrument.getName());
                return;
            }
            for (int i = 0; i < orderList.size(); i++) {
                WorkOrder workOrder = orderList.get(i);
                logTemInfo("workOrder cycle: " + i);
                logTemInfo("workOrder = " + (i + 1) + ": " + workOrder);
                model.setWorkOrderAsServed(workOrder.getId());
            }
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
    }

    @Override
    public void sendWorkOrdersBatch() throws InstrumentException {
        try {
            List<WorkOrder> orderList = model.getWorkOrdersByInstrumentWithFeeStatus(this.instrument.getId());
            if (orderList.size() > 0) {
                logTemInfo(String.format("There has %s workorders to send for " + instrument.getName(), orderList.size()));
            } else {
                logTemInfo("There no workorders to send for " + instrument.getName());
                //this.status = STATUS_MASTER;
                //this.stage = STAGE_TOKEN_TRANSFER;
                sendTokenTransfer(messageOut);
                return;
            }
            for (int i = 0; i < orderList.size(); i++) {
                WorkOrder workOrder = orderList.get(i);
                logTemInfo("workOrder cycle: " + i);
                logTemInfo("workOrder = " + (i + 1) + ": " + workOrder);
                if (sendWorkOrderBatch(workOrder)) {
                    model.setWorkOrderAsServed(workOrder.getId());
                } else {
                    throw new WorkOrderNotSendException("workOrder = " + (i + 1) + ": " + workOrder + " not send");
                }
            }
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
    }

    public boolean sendTokenTransfer(Advia2120Message message) throws MtIsNotInDiapazonException, InterruptedException, IOException, ContinueException, BreakException {
        messageOut = message.getTokenTransferMessage();
        threadSleep(TS);
        if (testMode) {
            outputStreamWriteLogTemp(messageOut.rawMessage);
        } else {
            outputStream.write(messageOut.rawMessage);
        }
        attempt++;
        threadSleep(TLS + 5000);
        if (inputStream.available() == 0) {
            status = STATUS_MASTER;
            stage = STAGE_INITIALIZATION;
            attempt = 1;
            throw new ContinueException("No mt from instrument after " + 5 + " sec. Go to Init");
        }
        byte incomeValue = testMode ? inputStreamReadLogTemp() : (byte) inputStream.read();
        if (incomeValue == ASCII.NACK) {
            logTemInfo("H<-M. NACK, attempt " + attempt);
            if (attempt < 3) {
                return sendTokenTransfer(message);
            } else {
                logTemInfo("Received NACK 2nd time. REINIT stage");
                this.stage = STAGE_INITIALIZATION;
                this.attempt = 1;
                threadSleep(WATCH_DOG);
                return false;
            }
        } else if (incomeValue != messageOut.getMessageToggle()) {
            logTemInfo("H<-M. Received wrong MT: " + incomeValue + ". REINIT stage");
            this.stage = STAGE_INITIALIZATION;
            this.attempt = 1;
            threadSleep(WATCH_DOG);
            return false;
        } else {
            this.attempt = 1;
            this.status = STATUS_SLAVE;
            return true;
        }
    }

    @Override
    public void saveResults(Object messageInput) throws InstrumentException {
        try {
            Advia2120Message messageInputAdvia = (Advia2120Message) messageInput;
            if (!messageInputAdvia.validateCheckSum()) {
                sendNack();
                saveResults(messageInput);
            } else {
                ResultMessage resMessage = new ResultMessage(messageInputAdvia.getRawMessage());
                threadSleep(TLS);
                outputStream.write(messageInputAdvia.getMessageToggle());
                logMtToInstrument(messageInputAdvia.getMessageToggle());
                threadSleep(1500);
                attempt = 1;
                if (sendResultValidationMessage(messageInputAdvia)) {
                    List<entity.Result> results = getResultsFromMessage(resMessage);
                    for (Result result : results) {
                        model.insertObject(result);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("<<<<<<<<<<");
            ex.printStackTrace();
            System.out.println(">>>>>>>>>>>>>>");
            throw new Advia2120Exception(ex);
        }
    }

    private boolean sendResultValidationMessage(Advia2120Message inputMessage) throws MtIsNotInDiapazonException, IOException, BreakException {
        messageOut = inputMessage.getResultValidationMessage();
        outputStreamWrite(messageOut.getRawMessage());
        threadSleep(TLS);
        byte incomeValue = inputStreamReadLogTemp();
        if (incomeValue == NACK) {
            logTemp("info", "H<-M Received Nack", true);
            if (attempt < 3) {
                sendResultValidationMessage(inputMessage);
            } else {
                stage = STAGE_INITIALIZATION;
                status = STATUS_MASTER;
                attempt = 1;
                threadSleep(WATCH_DOG);
                return false;
            }
        } else if (incomeValue != messageOut.getMessageToggle()) {
            logTemp("info", "H<-M. Received wrong MT: " + incomeValue + ". REINIT stage", true);
            stage = STAGE_INITIALIZATION;
            status = STATUS_MASTER;
            attempt = 1;
            threadSleep(WATCH_DOG);
            return false;
        } else {
            status = STATUS_SLAVE;
            return true;
        }
        return false;
    }

    public static byte[] readMessage(InputStream inputStream, int firstValue) throws IOException {
        if (firstValue != ASCII.STX) {
            return null;
        }
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
                break;

            }
        }
        return neewByteArray;
    }

    @Override
    public boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException {
        if (batchOrder) {
            messageOut = messageOut.getMessageWorkOrder(workOrder);
        } else {
            workOrder.setRack("");
            workOrder.setPosition(0);
            messageOut = messageInput.getMessageWorkOrder(workOrder);
        }
        this.attempt = 1;
        return downloadWorkorder(messageOut);
    }

    public void sendNoWorkOrder(String sid) throws InstrumentException {
        messageOut = messageInput.getMessageNoWorkOrder(sid);
        this.attempt = 1;
        downloadWorkorder(messageOut);
    }

    @Override
    public List<Result> getResultsFromMessage(Object messageObj) throws InstrumentException {
        List<entity.Result> listResultEntity = new ArrayList<>();
        try {
            ResultMessage message = (ResultMessage) messageObj;
            List<ResultAdvia2120> listResultAdvia2120 = message.getResults();
            //WorkOrder wo;
            for (ResultAdvia2120 result : listResultAdvia2120) {
                Result resEntity = new Result();
                String testCode = result.getTest().trim();
                String testValue = result.getValue().trim();
                entity.Test test = model.getTestByTestCode(testCode, this.instrument.getId());
                //wo = model.getWorkOrderBySidAndInstrument(message.getSid(), this.instrument.getId());
                //wo = new WorkOrder();
                //wo.setId(0);
                resEntity.setWorkOrderId(0);
                resEntity.setTestCode(testCode);
                resEntity.setParameterId(test.getParameter().getId());
                resEntity.setValue(testValue);
                resEntity.setInstrument(instrument.getName());
                resEntity.setSid(message.getSid());
                int lastVersion = model.getVersionOfLastResultByInstrumentAndSid(instrument.getName(), message.getSid(), testCode);
                resEntity.setVersion(lastVersion + 1);
                listResultEntity.add(resEntity);
            }

        } catch (Exception ex) {
            throw new Advia2120Exception(ex);
        }
        return listResultEntity;
    }

    private void sendNack() throws ContinueException {
        try {
            threadSleep(TLS);
            System.out.println("H->M. Message has wrong chk. Sending NACK To Instrument");
            outputStream.write(ASCII.NACK);
            log(TO_INSTRUMENT, ASCII.getASCIICodeAsString(ASCII.NACK), true);
            throw new ContinueException("Message has wrong chk. Sending NACK To Instrument");
        } catch (Exception ex) {
            throw new ContinueException(ex);
        }
    }

    public boolean downloadWorkorder(Advia2120Message messageOut) throws InstrumentException {
        try {
            outputStream.write(messageOut.getRawMessage());
            log(TO_INSTRUMENT, messageOut.getMessageAsString(), true);
            System.out.println("H->M. WorkOrder message sent, attempt " + new Date() + attempt++ + messageOut.getMessageAsString());
            threadSleep(TLS);
            byte incomeValue = (byte) inputStream.read();
            logTemp(TO_HOST, ASCII.getASCIICodeAsString(incomeValue), true);
            if (incomeValue == ASCII.NACK) {
                System.out.println("Recived Nack");
                if (attempt < 3) {
                    outputStream.write(messageOut.getRawMessage());
                    System.out.println("H->M. NoWorkOrder message sent, attempt " + attempt++ + messageOut.getMessageAsString());
                    attempt++;
                    //downloadWorkorder();
                    downloadWorkorder(messageOut);
                } else {
                    System.out.println("H<-M. Nack: " + incomeValue + ". REINIT stage");
                    attempt = 1;
                    return false;
                }
            } else if (incomeValue != messageOut.getMessageToggle()) {
                System.out.println("H<-M. Received wrong MT: " + incomeValue + ". REINIT stage");
                attempt = 1;
                return false;
            } else {
                System.out.println("H<-M. ACK, MT=" + incomeValue);
                incomeValue = (byte) inputStream.read();
                if (incomeValue == STX) {
                    messageInput = new Advia2120Message(readMessage(inputStream, incomeValue));
                    log(TO_HOST, messageInput.getMessageAsString(), true);
                    Advia2120Message messageOutput;
                    System.out.println("H<-M. " + messageInput.getMessageAsString());
                    if (messageInput.getType().equals("E")) {
                        if (messageInput.validateCheckSum()) {
                            String errorCode = (char) messageInput.getRawMessage()[11] + "" + (char) messageInput.getRawMessage()[12];
                            if (errorCode.equals(" 0") || errorCode.equals("10")) {
                                System.out.println("Work Order is valid");
                                outputStream.write(messageInput.getMessageToggle());
                                logMtToInstrument(messageInput.getMessageToggle());
                                threadSleep(TLS);
                                if (sendTokenTransfer(messageInput)) {
                                    status = STATUS_SLAVE;
                                    return true;
                                }
//                                messageOutput = messageInput.getTokenTransferMessage();
//                                outputStream.write(messageOutput.getRawMessage());
//                                log(TO_INSTRUMENT, messageOut.getMessageAsString(), true);
//                                System.out.println("now sending Token Transfer" + messageOutput.getMessageAsString());
//                                incomeValue = (byte) inputStream.read();
//                                if (incomeValue == messageOutput.getMessageToggle()) {
//                                    System.out.println("H<-M. ACK, MT=" + incomeValue);
//                                    status = STATUS_SLAVE;
//                                    return true;
//                                } else {
//                                    System.out.println("H<-M. GO out. Wrong MT=" + incomeValue);
//                                    return false;
//                                }
                            } else {
                                logTemInfo("Work Order is not valid, errorCode: " + errorCode + ". Invalid test number");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new InstrumentException(ex);
        }
        return false;
    }

    private void logMtToInstrument(byte mt) {
        log(TO_INSTRUMENT, "MT: " + mt, true);
    }

    private void logMtToInstrumentTemp(byte mt) {
        logTemp(TO_INSTRUMENT, "MT: " + mt, true);
    }

}
