package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public class PageDelete extends AbstractPageNavigator {

    public PageDelete(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            if (this.entity != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                switch (entity) {
                    case "order":
                        model.removeObject(id, new WorkOrder());
                        request.setAttribute("message", String.format("Задание с номером %s удалено", id));

                        break;
                    case "instrument":
                        model.removeObject(id, new Instrument());
                        request.setAttribute("message", String.format("Инструмент с номером %s удален", id));
                        break;
                    default:
                        throw new UnsupportedOperationException("in PageDelete method doSomething()");
                }
                PrepareNavigate prepareNavigate = new PrepareNavigate(request, model, entity);
                this.pageTo = prepareNavigate.setAttributesGetPageTo();
            }
        } catch (Exception ex) {
            Logger.getLogger(PageDelete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
