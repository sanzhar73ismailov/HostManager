package lab.servlet.navig;

import entity.Analysis;
import entity.Parameter;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;
import modelHost.ModelException;

public class PageEditParameter extends PageEditAbstract {

    public PageEditParameter(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            this.pageTo = "parameter.jsp";
            Parameter bean = new Parameter();
            List<Analysis> listAnalyses = model.getObjects(new Properties(), new Analysis());
            if (request.getParameter("id") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                bean = model.getObject(id, new Parameter());
            }
            //List<InstrumentModel> dicInstrModel = model.getObjects(null, new InstrumentModel());
           // request.setAttribute("modelList", dicInstrModel);
            request.setAttribute("analyses", listAnalyses);
            request.setAttribute("bean", bean);
            
        } catch (ModelException ex) {
            throw new NavigatorException(ex);
        }
    }

}
