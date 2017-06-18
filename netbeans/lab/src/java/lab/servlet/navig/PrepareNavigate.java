package lab.servlet.navig;

import entity.Analysis;
import entity.Instrument;
import entity.LogInstrument;
import entity.Parameter;
import entity.Test;
import entity.WorkOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import modelHost.Model;
import modelHost.ModelException;

public class PrepareNavigate {

    private HttpServletRequest request;
    private Model model;
    String entity;
    private static int NUMBER_LOGS_SHOW = 1000;

    public PrepareNavigate(HttpServletRequest request, Model model, String entity) {
        this.request = request;
        this.model = model;
        this.entity = entity;
    }

    public String setAttributesGetPageTo() throws NavigatorException {
        String pageTo = null;
        int instrId = 0;
        Instrument instrument = new Instrument(0, "All");
        String pageTitle = "Noname title";
        try {
            if (entity == null) {
                throw new NavigatorException("entity is null (not defined)");
            }
            switch (this.entity) {
                case "instrument":
                    List<Instrument> instruments = model.getInstruments();
                    request.setAttribute("listInstruments", instruments);
                    pageTo = "listInstruments.jsp";
                    pageTitle = "list of instruments";
                    break;
                case "order":
                    instrId = Integer.parseInt(request.getParameter("instrument"));
                    List<WorkOrder> listWorkOrders = model.getWorkOrdersByInstrument(instrId);
                    List<entity.Test> dicTests = model.getTestsByInstrument(instrId, " order by test_order");
                    //createWo = FabricaOrder.createWorkOrder(instr.getModel().getName());
                    request.setAttribute("listWorkOrders", listWorkOrders);
                    request.setAttribute("dicTests", dicTests);
                    instrument = model.getObject(instrId, new Instrument());
                    request.setAttribute("instrument", instrument);
                    pageTo = "listOrders.jsp";
                    pageTitle = "list of orders of " + instrument.getName();
                    break;
                case "log":
                    List<LogInstrument> listLogs = null;
                    instrId = Integer.parseInt(request.getParameter("instrument"));
                    instrument = model.getObject(instrId, new Instrument());
                    request.setAttribute("instrument", instrument);
                    String tempOff = request.getParameter("temp");
                    String deleteTemp = request.getParameter("deleteTemp");
                    if (tempOff != null && tempOff.equals("off")) {
                        listLogs = model.getLogsByInstrumentNoTemp(instrId, "order by id desc", NUMBER_LOGS_SHOW);
                        request.setAttribute("message", "shows without temp logs");
                    } else if (deleteTemp != null && deleteTemp.equals("1")) {
                        int deltedRows = model.deleteTemLogsByInstrument(new Instrument(instrId, ""));
                        request.setAttribute("message", "deleted " + deltedRows + " rows");
                        listLogs = model.getLogsByInstrument(instrId, "order by id desc", NUMBER_LOGS_SHOW);
                    } else {
                        listLogs = model.getLogsByInstrument(instrId, "order by id desc", NUMBER_LOGS_SHOW);
                    }
                    request.setAttribute("listLogs", listLogs);
                    if (request.getParameter("autoRefresh") != null && request.getParameter("autoRefresh").equals("1")) {
                        request.setAttribute("autoRefresh", "1");
                        request.setAttribute("autoRefreshMessage", "Auto Refresh On");
                    }
                    pageTo = "listLogs.jsp";
                    pageTitle = "log: " + instrument.getName();
                    break;
                case "parameter":
                    String analysisStrId = request.getParameter("analysis_id");
                    String instrStrId = request.getParameter("instrument");
                    ParameterInsrumentForPage parameterInsrumentForPage = new ParameterInsrumentForPage(instrStrId, analysisStrId, model);
                    request.setAttribute("listObjects", parameterInsrumentForPage.getListParameters());
                    request.setAttribute("listInstruments", parameterInsrumentForPage.getListInstruments());
                    request.setAttribute("analysis", parameterInsrumentForPage.getAnalysis());
                    request.setAttribute("instrument", parameterInsrumentForPage.getInstrument());
                    pageTo = "listParameters.jsp";
                    pageTitle = "list of parameters";
                    break;
                case "sql":
                    pageTo = "sql.jsp";
                    pageTitle = "sql manager";
                    break;
                default:
                    throw new UnsupportedOperationException("unknow entity: " + entity);
            }
            request.setAttribute("pageTitle", pageTitle);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new NavigatorException(ex);
        }

        return pageTo;
    }

}
