package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.web.lab.servlet.navig.PageStartStopApparatus;
import kz.biostat.lishostmanager.web.lab.servlet.navig.AbstractPageNavigator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.web.lab.servlet.navig.ListNavigator;
import kz.biostat.lishostmanager.web.lab.servlet.navig.NavigatorException;
import kz.biostat.lishostmanager.web.lab.servlet.navig.Page404;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageChangeStatus;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageDelete;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageException;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageIndex;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageSql;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

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
