/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.util.List;
import javax.servlet.ServletRequest;

/**
 *
 * @author sanzhar.ismailov
 */
class CreateWorkOrderForDimensionXPand implements CreateWorkOrder {

    public CreateWorkOrderForDimensionXPand() {
    }

    @Override
    public WorkOrder parseRequestToWorkOrder(ServletRequest request) {
        WorkOrder order = new WorkOrder();
        order.setId(Integer.parseInt(request.getParameter("id")));
        Instrument instr = new Instrument(Integer.parseInt(request.getParameter("instrument")), null);
        order.setInstrument(instr);
        if (request.getParameter("mid") != null && !request.getParameter("mid").trim().isEmpty()) {
            order.setMid(Integer.parseInt(request.getParameter("mid")));
        } else {
            order.setMid(1);
        }
        order.setSid(request.getParameter("sid"));
        String sampleType = "ser";
        if (request.getParameter("sampleType") != null && !request.getParameter("sampleType").isEmpty()) {
            sampleType = request.getParameter("sampleType");
        }
        order.setSampleType(sampleType);
        order.setPatientName(request.getParameter("patientName"));
//        order.setPatientNumber(request.getParameter("patientNumber"));

        String[] testVals = request.getParameterValues("tests");
        if (testVals != null) {
            StringBuilder sb = new StringBuilder();
            for (String string : testVals) {
                sb.append(",").append(string);
            }
            order.setTests(sb.substring(1));
        }
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
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    @Override
    public String getPageForm() {
        return "dimensionXpand";
    }
}
