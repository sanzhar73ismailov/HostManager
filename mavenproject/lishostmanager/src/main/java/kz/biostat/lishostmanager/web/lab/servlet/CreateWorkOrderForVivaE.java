package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;

public class CreateWorkOrderForVivaE implements CreateWorkOrder {

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPageForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // return "vivaE";
    }

}
