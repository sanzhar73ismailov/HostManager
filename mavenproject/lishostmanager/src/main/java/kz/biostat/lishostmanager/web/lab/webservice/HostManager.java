package kz.biostat.lishostmanager.web.lab.webservice;

import kz.biostat.lishostmanager.comport.entity.*;
import java.util.*;
import kz.biostat.lishostmanager.comport.instrument.ModeSystemWorking;
import kz.biostat.lishostmanager.comport.instrument.NoSuchSetting;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import kz.biostat.lishostmanager.comport.instrument.Settings;
import kz.biostat.lishostmanager.comport.modelHost.*;
import kz.biostat.lishostmanager.web.report.DoReport;

@WebService(serviceName = "HostManager")
public class HostManager {

    private Model model;
    private ModeSystemWorking modeSystemWorking;
    private HostManagerHelper hostManagerHelper;
    private boolean testMode = false;
    private static final String delimiterForSidIfBARCODE_NO = "-";

    private long id;

    public HostManager() {
        try {
            model = new ModelImpl();
            hostManagerHelper = new HostManagerHelper(model);
            //InstrumentIndicator.getInstance().getMapSettings()
        } catch (ModelException ex) {
            Logger.getLogger(HostManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "addWorkOrder")
    public String addWorkOrder(@WebParam(name = "message") String messageXml) {

        if (testMode) {
            System.out.println("message:" + messageXml);
        }
        XmlWork xmlWork = new XmlWork(messageXml);
        System.out.println("");
        if (1 == 0 && !xmlWork.validateBySchema()) {
            return xmlWork.getResponseMessage();
        }
        try {
            model = new ModelImpl();
            if (Settings.isBarcodeUsed()) {
                modeSystemWorking = ModeSystemWorking.BARCODE_YES;
            } else {
                modeSystemWorking = ModeSystemWorking.BARCODE_NO;
            }
            String incomeNumber = xmlWork.getIncomeNumber();

            if (testMode) {
                System.out.println("incomeNumber = " + incomeNumber);
            }
            String senderName = xmlWork.getSenderName();
            if (testMode) {
                System.out.println("senderName = " + senderName);
            }

            Message myMessage = model.getMessageBySenderAndIncomeNember(senderName, incomeNumber);
            if (testMode) {
                System.out.println("myMessage = " + myMessage);
            }
            boolean myMessageExist = false;
            if (myMessage == null) {
                myMessage = new Message();
                myMessage.setIncomeNumber(incomeNumber);
                myMessage.setSender(model.getSenderByName(senderName));
                int messId = model.insertMessage(myMessage, messageXml);
                //System.out.println("messId = " + messId);
                myMessage = model.getObject(messId, new Message());

            } else {
                myMessageExist = true;
                if (myMessage.isClose()) {
                    throw new ModelException(String.format("message with number %s and sender %s is closed", incomeNumber, senderName));
                } else {
                    model.updatetMessageHistoryByMessage(myMessage, messageXml);
                }
            }
            ParamsMessage params = xmlWork.parseStringMessageToParams();
            if (modeSystemWorking == ModeSystemWorking.BARCODE_NO) {
//                params.sid = params.sid + "_" + myMessage.getId();
                String dateForSid = "";
                try {
                    dateForSid = new SimpleDateFormat("yyyyMMdd").format(myMessage.getInsertDatetime());
                } catch (NumberFormatException mex) {
                    dateForSid = new SimpleDateFormat("yyyyMMdd").format(new Date());
                }
                params.sid = params.sid + delimiterForSidIfBARCODE_NO + dateForSid;
            }
            List<WorkOrder> listWo = hostManagerHelper.getWorkOrdersFromParams(myMessage, params, modeSystemWorking);
            for (WorkOrder workOrder : listWo) {
                if (myMessageExist) {
                    WorkOrder workOrderByGetBySidAndInstrument = model.getWorkOrderBySidAndInstrument(workOrder.getSid(), workOrder.getInstrument().getId());
                    if (workOrderByGetBySidAndInstrument != null) {
                        if (model.twoWorkOrdersEqualsByRackAndPosition(workOrder, workOrderByGetBySidAndInstrument)) {
                            workOrder.setId(workOrderByGetBySidAndInstrument.getId());
                            model.updateObject(workOrder);
                        } else {
                            throw new ModelException("the rack and position is wrong in DB");
                        }
                    } else {
                        model.insertObject(workOrder);
                    }
                } else {
                    if (!model.isExistWorkOrderWithSuchSidAndInstrument(workOrder)) {
                        model.insertObject(workOrder);
                    } else {
                        throw new ModelException(String.format("the workorder with "
                                + " sid %s is already exist", workOrder.getSid()));
                    }
                }
            }
        } catch (Exception ex) {
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
            return ex.getMessage();
        }
        return "1";
    }

    @WebMethod(operationName = "getResults")
    public String getResults(@WebParam(name = "messageId") String messageId,
            @WebParam(name = "sender") String sender) {
        BufferedReader reader = null;
        try {
            String str = null;
            //String str = HostManagerHelper.getRespXml();
            if (1 == 1) {

                Message message = model.getMessageBySenderAndIncomeNember(sender, messageId);
                List<Result> listResults = null;
                if (message == null) {
                    // проверяем, возможно результаты запрашиватся по сиду (SID), для односторонних
                    //аппаратов
                    String sid = messageId.substring(0, messageId.indexOf(" :"));
                    System.out.println("messageId = " + messageId);
                    System.out.println("sid = " + sid);

                    listResults = model.getResultsBySid(sid);
                    if (listResults.isEmpty()) {
                        throw new ModelException(String.format("no messages from %s with id %s", sender, messageId));
                    }
                } else {
                    Properties propTogetListWo = new Properties();
                    propTogetListWo.setProperty("mid", "=" + message.getId());
                    List<WorkOrder> listWo = model.getObjects(propTogetListWo, new WorkOrder());
                    listResults = model.getResultsByListWorkOrders(listWo);

                }
                str = hostManagerHelper.getResutResponseXML(listResults, message);
                //System.out.println("str = " + str);
                return str;
            } else {
                File file = new File("xsd/resultGet.xml");
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new FileReader(file));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
            }
        } catch (Exception ex) {
            Logger.getLogger(HostManager.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(HostManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @WebMethod(operationName = "getResultsByInstrumentAndSid")
    public String getResultsByInstrumentAndSid(@WebParam(name = "instrument") String instrument,
            @WebParam(name = "sid") String sid) {
        try {
            String str = null;
            //String str = HostManagerHelper.getRespXml();
            List<Result> listResults = model.getResultsByInstrumentAndSid(instrument, sid);
            str = hostManagerHelper.getResutResponseXML(listResults);
            System.out.println("str = " + str);
            return str;
        } catch (Exception ex) {
            Logger.getLogger(HostManager.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }

    @WebMethod(operationName = "getResultsByInstrumentAndDate")
    public String getResultsByInstrumentAndDate(@WebParam(name = "instrument") String instrument,
            @WebParam(name = "date") String date) {
        try {
            DoReport doReport = new DoReport(instrument, date);
            doReport.makeReport();
            String csv = doReport.getReportAsCSVString();
            if (testMode) {
                System.out.println("REPORT AS CSV START");
                System.out.println(csv);
                System.out.println("REPORT AS CSV FINISH");
            }
            return csv;
        } catch (Exception ex) {
            if (testMode) {
                ex.printStackTrace();
            }
            return ex.getMessage();
        }
    }

}
