package lab.servlet;

import entity.Instrument;
import entity.WorkOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;

public class CreateWorkOrderForAdvia2120 implements CreateWorkOrder {

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
        order.setRack(request.getParameter("rack"));
        if (request.getParameter("position") != null) {
            order.setPosition(Integer.parseInt(request.getParameter("position")));
        }
        order.setPatientName(request.getParameter("patientName"));
        order.setPatientNumber(request.getParameter("patientNumber"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (request.getParameter("dateBirth") != null && !request.getParameter("dateBirth").trim().isEmpty()) {
                order.setDateBirth(sdf.parse(request.getParameter("dateBirth")));
            }
            if (request.getParameter("dateCollection") != null && !request.getParameter("dateCollection").trim().isEmpty()) {
                order.setDateCollection(sdf.parse(request.getParameter("dateCollection")));
            }
        } catch (ParseException ex) {
            Logger.getLogger(Action.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (request.getParameter("sex") != null) {
            order.setSex(Integer.parseInt(request.getParameter("sex")));
        }
        if (request.getParameter("status") != null) {
            order.setSex(Integer.parseInt(request.getParameter("status")));
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
//        addParamProperties.setProperty("containerType", request.getParameter("containerType"));
         addParamProperties.setProperty("routineSampleOrStatSample", request.getParameter("routineSampleOrStatSample"));
         addParamProperties.setProperty("updateIndicator", request.getParameter("updateIndicator"));
//        addParamProperties.setProperty("dilution", request.getParameter("dilution"));
//        order.setAddParams(addParamProperties);
        return order;
    }

    @Override
    public String validate(WorkOrder order, List<WorkOrder> listWorkOrders) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPageForm() {
        return "advia2120";
    }


}
