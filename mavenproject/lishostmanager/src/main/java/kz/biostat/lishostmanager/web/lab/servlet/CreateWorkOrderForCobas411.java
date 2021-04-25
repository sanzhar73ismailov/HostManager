package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.comport.entity.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;

public class CreateWorkOrderForCobas411 implements CreateWorkOrder {

    @Override
    public WorkOrder parseRequestToWorkOrder(ServletRequest request) {
        WorkOrder order = new WorkOrder();
        order.setId(Integer.parseInt(request.getParameter("id")));
        Instrument instr = new Instrument(Integer.parseInt(request.getParameter("instrument")), null);
        order.setInstrument(instr);
        if (request.getParameter("mid") != null && !request.getParameter("mid").trim().isEmpty()) {
            order.setMid(Integer.parseInt(request.getParameter("mid")));
        }else{
            order.setMid(1);
        }
        order.setSid(request.getParameter("sid"));
        order.setRack(request.getParameter("diskNumber"));
        if (request.getParameter("position") != null) {
            order.setPosition(Integer.parseInt(request.getParameter("position")));
        }
        order.setSampleType(request.getParameter("sampleType"));
        order.setPatientName(request.getParameter("patientName"));
        order.setPatientNumber(request.getParameter("patientNumber"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (request.getParameter("dateBirth") != null) {
                order.setDateBirth(sdf.parse(request.getParameter("dateBirth")));
            }
            if (request.getParameter("dateCollection") != null) {
                order.setDateCollection(sdf.parse(request.getParameter("dateCollection")));
            }
        } catch (ParseException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (request.getParameter("sex") != null) {
            order.setSex(Integer.parseInt(request.getParameter("sex")));
        }
        if (request.getParameter("status") != null) {
            order.setStatus(Integer.parseInt(request.getParameter("status")));
        }


        String[] testVals = request.getParameterValues("tests");
        if (testVals != null) {
            StringBuilder sb = new StringBuilder();
            for (String string : testVals) {
                sb.append(",").append(string);
            }
            order.setTests(sb.substring(1));
        }
        Properties addParamProperties = new Properties();
        addParamProperties.setProperty("containerType", request.getParameter("containerType"));
        addParamProperties.setProperty("routineSampleOrStatSample", request.getParameter("routineSampleOrStatSample"));
        addParamProperties.setProperty("dilution", request.getParameter("dilution"));
        order.setAddParams(addParamProperties);
        return order;
    }

    @Override
    public String validate(WorkOrder order, List<WorkOrder> listWorkOrders) {
        StringBuilder sb = new StringBuilder();
        // на будущее делать проверку, нет ли в базе заказа за этот день с таким же SID
        for (WorkOrder workOrder : listWorkOrders) {
            if (order.getSid().equals(workOrder.getSid())) {
                sb.append("Задание с номером (sample ID) ").
                        append(order.getSid()).
                        append(" уже существует во временном списке").
                        append("<br>");
            }
            if (order.getRack().equals(workOrder.getRack()) && order.getPosition() == workOrder.getPosition()) {
                sb.append("Задание с номером штатива/диска (rack/disk) ").
                        append(order.getRack()).
                        append(" и номером позиции ").
                        append(order.getPosition()).
                        append(" уже существует во временном списке").
                        append("<br>\n");
            }
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    @Override
    public String getPageForm() {
        return "cobasE411";
    }
}
