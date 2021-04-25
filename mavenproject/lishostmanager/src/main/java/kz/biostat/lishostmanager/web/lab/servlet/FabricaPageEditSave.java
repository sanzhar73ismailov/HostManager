package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.web.lab.servlet.navig.PageSaveInstrument;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.web.lab.servlet.navig.NavigatorException;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageEditAbstract;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageEditInstrument;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageEditOrder;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageEditParameter;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageSaveAbstract;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageSaveOrder;
import kz.biostat.lishostmanager.web.lab.servlet.navig.PageSaveParameter;
import kz.biostat.lishostmanager.comport.modelHost.Model;

public class FabricaPageEditSave {

    public static PageEditAbstract createPageEdit(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        String entity = request.getParameter("entity");
        PageEditAbstract pageEdit = null;
        switch (entity) {
            case "order":
                pageEdit = new PageEditOrder(request, response, model);
                break;
            case "instrument":
                pageEdit = new PageEditInstrument(request, response, model);
                break;
            case "parameter":
                pageEdit = new PageEditParameter(request, response, model);
                break;
            default:
                throw new UnsupportedOperationException("Unknown entity (see createPageEdit() in FabricaPageEditSave class): " + entity);
        }

        return pageEdit;
    }

    public static PageSaveAbstract createPageSave(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        String entity = request.getParameter("entity");
        PageSaveAbstract pageSave = null;
        switch (entity) {
            case "order":
                pageSave = new PageSaveOrder(request, response, model);
                break;
            case "instrument":
                pageSave = new PageSaveInstrument(request, response, model);
                break;
            case "parameter":
                pageSave = new PageSaveParameter(request, response, model);
                break;
            default:
                throw new UnsupportedOperationException("Unknown entity (FabricaPageEditSave, createPageSave()): " + entity);
        }

        return pageSave;
    }
}
