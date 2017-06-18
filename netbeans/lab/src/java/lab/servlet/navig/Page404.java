package lab.servlet.navig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;

public class Page404 extends AbstractPageNavigator{

    public Page404(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    public Page404() throws NavigatorException {
        super(null, null, null);
    }
    
    

    @Override
    public void doSomething() throws NavigatorException {
        this.pageTo = "404.jsp";
    }

    
    
}
