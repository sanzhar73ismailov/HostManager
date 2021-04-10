package lab.servlet;

import lab.servlet.navig.PageStartStopApparatus;
import lab.servlet.navig.AbstractPageNavigator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lab.servlet.navig.ListNavigator;
import lab.servlet.navig.NavigatorException;
import lab.servlet.navig.Page404;
import lab.servlet.navig.PageChangeStatus;
import lab.servlet.navig.PageDelete;
import lab.servlet.navig.PageException;
import lab.servlet.navig.PageIndex;
import lab.servlet.navig.PageSql;
import modelHost.Model;
import modelHost.ModelImpl;

public class FabricaPageNavigator {

    static AbstractPageNavigator createPageNavigator(HttpServletRequest request, HttpServletResponse response) throws NavigatorException {
        AbstractPageNavigator pageNavigator = null;
        try {
            Model model = new ModelImpl();
            String path = request.getServletPath();
            switch (path) {
                case "/list":
                    pageNavigator = new ListNavigator(request, response, model);
                    break;
                case "":
                case "/index":
                    pageNavigator = new PageIndex(request, response, model);
                    break;
                case "/edit":
                    pageNavigator = FabricaPageEditSave.createPageEdit(request, response, model);
                    break;
                case "/save":
                    pageNavigator = FabricaPageEditSave.createPageSave(request, response, model);
                    break;
                case "/remove":
                    pageNavigator = new PageDelete(request, response, model);
                    break;
                case "/apparatus":
                    pageNavigator = new PageStartStopApparatus(request, response, model);
                    break;
                case "/changeStatus":
                    pageNavigator = new PageChangeStatus(request, response, model);
                    break;
                case "/sql":
                    pageNavigator = new PageSql(request, response, model);
                    break;
                default:
                    pageNavigator = new Page404();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            pageNavigator = new PageException(request, ex);
        }
        return pageNavigator;
    }
}
