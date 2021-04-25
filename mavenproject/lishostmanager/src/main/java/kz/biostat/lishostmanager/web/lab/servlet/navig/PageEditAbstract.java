package kz.biostat.lishostmanager.web.lab.servlet.navig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;

public abstract  class PageEditAbstract extends AbstractPageNavigator{

    public PageEditAbstract(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public abstract void doSomething() throws NavigatorException;

}
