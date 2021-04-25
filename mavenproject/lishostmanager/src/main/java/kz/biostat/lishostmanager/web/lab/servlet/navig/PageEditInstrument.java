package kz.biostat.lishostmanager.web.lab.servlet.navig;

import kz.biostat.lishostmanager.comport.entity.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;

public class PageEditInstrument extends PageEditAbstract {

    public PageEditInstrument(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            this.pageTo = "instrument.jsp";
            Instrument bean = new Instrument();
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                bean = model.getObject(id, new Instrument());
            }
            List<InstrumentModel> dicInstrModel = model.getObjects(null, new InstrumentModel());
            request.setAttribute("modelList", dicInstrModel);
            request.setAttribute("bean", bean);
            
        } catch (ModelException ex) {
            throw new NavigatorException(ex);
        }
    }
}
