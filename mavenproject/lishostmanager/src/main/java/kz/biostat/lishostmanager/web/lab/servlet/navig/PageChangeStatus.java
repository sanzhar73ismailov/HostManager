package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public class PageChangeStatus extends AbstractPageNavigator {

    public PageChangeStatus(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));
            String label = "";
            if (status == 1) {
                model.setWorkOrderAsServed(id);
                label = "Обработанный";
            } else {
                model.setWorkOrderAsFree(id);
                label = "Свободный";
            }
            request.setAttribute("message", String.format("Статус задания с номером %s изменен на '%s'", id, label));
            request.setAttribute("order", model.getObject(id, new WorkOrder()));
            PrepareNavigate prepareNavigate = new PrepareNavigate(request, model, "order");
            this.pageTo = prepareNavigate.setAttributesGetPageTo();
        } catch (ModelException ex) {
            throw new NavigatorException(ex);
        }
    }
}
