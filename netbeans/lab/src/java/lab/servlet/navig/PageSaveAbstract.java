package lab.servlet.navig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;

public abstract class PageSaveAbstract extends AbstractPageNavigator {

    public PageSaveAbstract(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public abstract void doSomething() throws NavigatorException;
}
