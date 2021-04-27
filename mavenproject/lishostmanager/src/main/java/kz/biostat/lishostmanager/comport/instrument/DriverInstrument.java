package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.Result;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public abstract class DriverInstrument {

    protected OutputStream outputStream;
    protected InputStream inputStream;
    Socket echoSocket = null;
    protected Instrument instrument;
    protected Model model;
    public static final String TO_HOST = "H<-M";
    public static final String TO_INSTRUMENT = "H->M";
    public static final int NUMBER_SECONDS_IF_CONNECT_REFUSED = 60;
    protected boolean testMode;
    private static int SIZE_MESSAGE = 65500;

    public DriverInstrument(Instrument instrument, Model model) {
        this.instrument = instrument;
        this.model = model;
        this.testMode = instrument.isTestMode();
    }

    public abstract void driverRun() throws InstrumentException, IOException;

    public void mainRun() throws InstrumentException, ModelException {
        log("info", "Client " + instrument.getName() + " (LIS) running", true);
        if (isStoppedMashine()) {
            return;
        }
        log("info", "Try to connect... to " + instrument.getIp() + ", port " + instrument.getPort(), true);

        try {
            echoSocket = new Socket(instrument.getIp(), instrument.getPort());
            log("info", " " + (echoSocket.isConnected() ? "connected" : "not connected"), true);
            outputStream = echoSocket.getOutputStream();
            inputStream = echoSocket.getInputStream();
            while (true) {
                try {
                    driverRun();
                } catch (ContinueException ex) {
                    log("InstrContinueException", ex.getMessage(), true);
                } catch (BreakException ex) {
                    log("InstrBreakException", ex.getMessage(), true);
                    InstrumentIndicator.getInstance().stopInstrument(instrument.getId());
                    break;
                } catch (InstrumentException ex) {
                    log("InstrumentException", ex.getMessage(), true);
                    if (!(ex.getCause() instanceof SocketException)) {
                        InstrumentIndicator.getInstance().stopInstrument(instrument.getId());
                    }
                    logPrintStackTraceTemp(ex);
                    break;
                }
            }
        } catch (UnknownHostException ex) {
            runAgain(ex, "Don't know about host " + instrument.getIp());
        } catch (IOException ex) {
            closeStreams();
            runAgain(ex, "");
        } finally {
            closeStreams();
        }
    }

    private void closeStreams() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (echoSocket != null && !echoSocket.isClosed()) {
                echoSocket.close();
            }
        } catch (Exception ex) {
            logTemp("exception", ex.getMessage(), false);
        }
    }

    private void runAgain(Exception ex, String message) throws InstrumentException, ModelException {
        if (isStoppedMashine()) {
            logTemp("info", "stop mashine: " + instrument.getName(), true);
            return;
        } else {
            logTemp("exception", message + ", " + ex.getMessage() + ", next attempt in " + NUMBER_SECONDS_IF_CONNECT_REFUSED + " seconds", true);
        }
        try {
            Thread.sleep(NUMBER_SECONDS_IF_CONNECT_REFUSED * 1000);
        } catch (InterruptedException ex1) {
            logTemp("exception", ex1.getMessage(), true);
        }
        mainRun();
    }

    public void mainRunInNewThread() throws InstrumentException, ModelException {
        if (!instrument.isActive()) {
            log("info", instrument.getName() + " is not started, because not active", true);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mainRun();
                } catch (InstrumentException ex) {
                    Logger.getLogger(DriverCobas411.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ModelException ex) {
                    Logger.getLogger(DriverCobas411.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        InstrumentIndicator.getInstance().addThreadToMapInstruments(instrument.getId(), thread);
        thread.start();
    }

    protected boolean isStoppedMashine() {
        if (InstrumentIndicator.getInstance().isStopped(instrument.getId())) {
            System.out.println("      <<<<<<<< Instrument: " + instrument.getName()
                    + " (" + instrument.getId() + ")" + " stoped");
            return true;
        }
        return false;
    }

    protected void log(String direction, String message, boolean addToDb) {
        try {
            log(this.instrument.getId(), direction, message, addToDb);
        } catch (ModelException ex) {
            Logger.getLogger(DriverCobas411.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void logPrintStackTraceTemp(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        String strPrStackTrace = errors.toString();
        logTemp("stack trace", "<pre>" + strPrStackTrace + "</pre>", true);
    }

    protected void logTemp(String direction, String message, boolean addToDb) {
        try {
            logTemp(this.instrument.getId(), direction, message, addToDb);
        } catch (ModelException ex) {
            Logger.getLogger(DriverCobas411.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void logTemInfo(String message) {
        logTemp("info", message, true);
    }

    protected void log(int instrumentId, String direction, String message, boolean addToDb) throws ModelException {
        logGeneral(instrumentId, direction, message, addToDb, false);
    }

    protected void logTemp(int instrumentId, String direction, String message, boolean addToDb) throws ModelException {
        logGeneral(instrumentId, direction, message, addToDb, true);
    }

    private void logGeneral(int instrumentId, String direction, String message, boolean addToDb, boolean temp) throws ModelException {
        if (direction != null && direction.length() > 20) {
            direction = direction.substring(0, 20);
        }

//        System.out.println("log: " + direction + " " + message);
        Logger.getLogger(DriverInstrument.class.getName()).log(Level.INFO, "log: " + direction + " " + message);

        //чтобы в базу поместилось, ставим ограничение до SIZE_MESSAGE символов
        if (message != null && message.length() > SIZE_MESSAGE) {
            message = message.substring(0, SIZE_MESSAGE);
        }
        if (addToDb) {
            model.addLog(instrumentId, direction, message, temp);
        }
    }

    public boolean existOrders() throws InstrumentException, ModelException {
        List<WorkOrder> orderList = model.getWorkOrdersByInstrumentWithFeeStatus(this.instrument.getId());
        return orderList.size() > 0;
    }

    public void sendWorkOrdersBatch() throws InstrumentException {
        try {
            List<WorkOrder> orderList = model.getWorkOrdersByInstrumentWithFeeStatus(this.instrument.getId());
            if (orderList.size() > 0) {
                if (testMode) {
                    logTemp("info", String.format("There has %s workorders to send for " + instrument.getName(), orderList.size()), true);
                }
            }
            for (int i = 0; i < orderList.size(); i++) {
                WorkOrder workOrder = orderList.get(i);
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

    protected boolean sendWorkOrderBatch(WorkOrder workOrder) throws InstrumentException {
        return sendWorkOrder(workOrder, true);
    }

    protected boolean sendWorkOrderReply(WorkOrder workOrder) throws InstrumentException {
        return sendWorkOrder(workOrder, false);
    }

    public abstract boolean sendWorkOrder(WorkOrder workOrder, boolean batchOrder) throws InstrumentException;

    public abstract List<Result> getResultsFromMessage(Object message) throws InstrumentException;

    public void logIsAlredyRun() {
        log("info", instrument.getName() + " can not be run because alredy run", true);
    }

    protected void outputStreamWrite(byte value) throws IOException {
        outputStream.write(value);
        log(TO_INSTRUMENT, ASCII.getASCIICodeAsString(value), true);
    }

    protected void outputStreamWriteLogTemp(byte value) throws IOException {
        outputStream.write(value);
        logTemp(TO_INSTRUMENT, ASCII.getASCIICodeAsString(value), true);
    }

    protected void outputStreamWriteLogTemp(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        logTemp(TO_INSTRUMENT, getHTMLString(bytes), true);
    }

    protected void outputStreamWrite(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        log(TO_INSTRUMENT, getHTMLString(bytes), true);
    }

    private String getHTMLString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(ASCII.getASCIICodeAsString(aByte));
        }
        return sb.toString();
    }

    protected byte inputStreamRead() throws IOException {
        byte val = (byte) inputStream.read();
        if (val == STX) {
            logTemp(TO_HOST, ASCII.getASCIICodeAsString(val), true);
        } else {
            log(TO_HOST, ASCII.getASCIICodeAsString(val), true);
        }
        return val;
    }

    protected byte inputStreamReadLogTemp() throws IOException {
        byte val = (byte) inputStream.read();
        logTemp(TO_HOST, ASCII.getASCIICodeAsString(val), true);
        return val;
    }

    protected void threadSleep(long mls) throws BreakException {
        try {
            Thread.sleep(mls);
        } catch (InterruptedException ex) {
//            System.out.println("Thread.interrupted() = " + Thread.interrupted());
            // if (Thread.interrupted()) {
//            System.out.println("Thread was InterruptedException");
//            System.out.println("Now it's interrupt status is " + Thread.currentThread().isInterrupted());
            logTemp("sleep was interrupted", ex.getMessage(), true);
            if (isStoppedMashine()) {
                throw new BreakException("mashine " + instrument.getName() + " stopped");
            }
            // }
        }
    }

    /**
     *
     * Отправка тестового ENQ для ASTM протоколов - (Посылаем ENQ, получаем ACK,
     * посылаем EOT)
     */
    protected void sendTestASTMEnq() throws IOException, BreakException {
//        outputStreamWriteLogTemp(ENQ);
        outputStream.write(ENQ);
        threadSleep(2000);
        if (inputStream.available() == 0) {
            logTemp("info", "no answer after ENQ sending", true);
            return;
        }
        byte inputValueMainRun = (byte) inputStream.read();
//        byte inputValueMainRun = inputStreamRead();
        if (inputValueMainRun == ACK) {
//            outputStreamWrite(EOT);
            outputStream.write(EOT);
            logTemp("info", "ENQ-ACK-EOT is OK", true);
        }
    }

    public abstract void saveResults(Object inputMessage) throws InstrumentException;

}
