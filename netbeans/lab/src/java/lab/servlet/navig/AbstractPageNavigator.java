package lab.servlet.navig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public abstract class AbstractPageNavigator {

    protected String pageTo;
    protected String pageTitle;
    protected String entity;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Model model;
    String folder = "WEB-INF/view/";

    public AbstractPageNavigator(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        this.request = request;
        this.response = response;
        this.model = model;
        if (request != null) {
            this.entity = request.getParameter("entity");
        }
        doSomething();
        this.pageTo = folder + this.pageTo;
    }

    public String getPageTo() {
        return pageTo;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public abstract void doSomething() throws NavigatorException;
}
