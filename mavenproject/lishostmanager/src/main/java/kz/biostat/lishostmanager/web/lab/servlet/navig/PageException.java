package kz.biostat.lishostmanager.web.lab.servlet.navig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;

public class PageException extends AbstractPageNavigator {

    private Exception ex;

    public PageException(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    public PageException(HttpServletRequest request, Exception ex) throws NavigatorException {
        super(null, null, null);
        this.ex = ex;
        request.setAttribute("message", ex.getMessage());
    }

    @Override
    public void doSomething() throws NavigatorException {
        this.pageTo = "exception.jsp";
    }
}
