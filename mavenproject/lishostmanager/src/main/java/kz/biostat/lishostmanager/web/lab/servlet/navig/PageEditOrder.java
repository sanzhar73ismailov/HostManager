package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.Configurator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public class PageEditOrder extends PageEditAbstract {

    public PageEditOrder(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            this.pageTo = "order.jsp";
            WorkOrder workOrder = null;
            int instrId = Integer.parseInt(request.getParameter("instrument"));
            Instrument instrObj = model.getObject(instrId, new Instrument());
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                workOrder = model.getObject(id, new WorkOrder());
            } else {
                int postion = 1;
                workOrder = new WorkOrder();
                if (request.getParameter("positionLast") != null && !request.getParameter("positionLast").isEmpty()) {
                    postion = Integer.parseInt(request.getParameter("positionLast"));
                    if (postion == 30) {
                        postion = 1;
                    } else {
                        postion++;
                    }
                }
                workOrder.setPosition(postion);
                workOrder.setInstrument(new Instrument(instrId, null));
                workOrder.getAddParams().setProperty("routineSampleOrStatSample", "R");
            }
            String instrument = instrObj.getModel().getName();
            String pageForm = "";
            switch (instrument) {
                case Configurator.ADVIA2120:
                    pageForm = "advia2120";
                    break;
                case Configurator.COBASE411:
                    pageForm = "cobasE411";
                    break;
                case Configurator.IMMULITE2000:
                    pageForm = "immulite2000";
                    break;
                case Configurator.ADVIA_CENTAUR_CP:
                    pageForm = "adviaCentaurCp";
                    break;
                case Configurator.DIMENSION_XPAND:
                    pageForm = "dimensionXpand";
                    break;
                case Configurator.VIVA_E:
                    pageForm = "vivaE";
                    break;
                case Configurator.SYSMEX_CA_660:
                    pageForm = "sysmexCA660";
                    break;
                case Configurator.SYSMEX_CA_1500:
                    pageForm = "sysmexCA1500";
                    break;
                default:
                    throw new NavigatorException("unknown instrument page (see PageEditOrder doSomething() method)");

            }
            List<kz.biostat.lishostmanager.comport.entity.Test> dicTests = model.getTestsByInstrument(instrId, " order by test_order");
            request.setAttribute("order", workOrder);
            request.setAttribute("dicTests", dicTests);
            request.setAttribute("formName", pageForm);
            request.setAttribute("instrument", instrObj);
        } catch (ModelException ex) {
            throw new NavigatorException(ex);
        }
    }
}
