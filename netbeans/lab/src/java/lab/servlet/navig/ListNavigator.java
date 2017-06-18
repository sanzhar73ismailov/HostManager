package lab.servlet.navig;

import entity.Instrument;
import entity.LogInstrument;
import entity.WorkOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;
import modelHost.ModelException;

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
