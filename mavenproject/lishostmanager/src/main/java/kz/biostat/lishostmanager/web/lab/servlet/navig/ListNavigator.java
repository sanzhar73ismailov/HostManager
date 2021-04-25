package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.LogInstrument;
import kz.biostat.lishostmanager.comport.entity.WorkOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public class ListNavigator extends AbstractPageNavigator {

    public ListNavigator(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);

    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            pageTo = "404.jsp";
            PrepareNavigate prepareNavigate = new PrepareNavigate(request, model, entity);
            this.pageTo = prepareNavigate.setAttributesGetPageTo();
        } catch (Exception ex) {
            throw new NavigatorException(ex);
        }

    }
}
