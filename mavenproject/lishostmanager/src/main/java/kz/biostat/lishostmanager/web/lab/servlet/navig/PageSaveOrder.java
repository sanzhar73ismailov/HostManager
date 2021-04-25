package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.biostat.lishostmanager.web.lab.servlet.CreateWorkOrder;
import kz.biostat.lishostmanager.web.lab.servlet.FabricaOrder;
import kz.biostat.lishostmanager.comport.modelHost.Model;

public class PageSaveOrder extends PageSaveAbstract {

    public PageSaveOrder(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {

        WorkOrder order = null;
        try {
            String instrumentAsStr = request.getParameter("instrument");
            List<kz.biostat.lishostmanager.comport.entity.Test> dicTests = null;
            CreateWorkOrder createWo = null;
            int instrumentId = 0;
            if (instrumentAsStr != null) {
                instrumentId = Integer.parseInt(instrumentAsStr);
                Instrument instrObj = model.getObject(instrumentId, new Instrument());
                createWo = FabricaOrder.createWorkOrder(instrObj.getModel().getName());

                order = createWo.parseRequestToWorkOrder(request);
                String message = null;
                int id = 0;
                if (order.getId() == 0) {
                    //message = createWo.validate(order, listWorkOrders);
                    if (message == null) {
                        id = model.insertObject(order);
                        order = model.getObject(id, new WorkOrder());
                    }
                } else {
                    model.updateObject(order);
                }
                request.setAttribute("order", order);
                if (message != null) {
                    request.setAttribute("message", message);
                    this.pageTo = "order.jsp";
                } else {
                    PrepareNavigate prepareNavigate = new PrepareNavigate(request, model, entity);
                    this.pageTo = prepareNavigate.setAttributesGetPageTo();
                }
            } else {
                throw new NavigatorException("instrument not defined");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new NavigatorException(ex);
            /*
            this.pageTo = "order.jsp";
            request.setAttribute("message", ex.getMessage());
            request.setAttribute("order", order);
            * */
        }
    }
}
