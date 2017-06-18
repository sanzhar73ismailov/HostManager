package lab.servlet.navig;

import entity.Analysis;
import entity.Parameter;
import entity.Test;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;
import modelHost.ModelException;

public class PageSaveParameter extends PageSaveAbstract {

    public PageSaveParameter(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            this.pageTo = "listParameters.jsp";
            String id = request.getParameter("id");
            String name = null;
            name = request.getParameter("name");
            /*
            // использовал раньше для сохранения русс текста, теперь создал файл glassfish-web.xml
            // с параметром <parameter-encoding default-charset="UTF-8" />
             try {
             name = new String(request.getParameter("name").getBytes("ISO8859_1"), "UTF-8");
             } catch (UnsupportedEncodingException ex) {
             Logger.getLogger(PageSaveParameter.class.getName()).log(Level.SEVERE, null, ex);
             }
             */

            //System.out.println("name = " + name);
            String testId = request.getParameter("testId");
            Parameter bean = new Parameter();
            bean.setId(Integer.parseInt(id));
            bean.setName(name);
            Test testDefault = new Test();
            testDefault.setId(Integer.parseInt(testId));
            bean.setTestDefault(testDefault);
            Analysis analysis = new Analysis();
            int analId = Integer.parseInt(request.getParameter("analysisId"));
            analysis.setId(analId);
            bean.setAnalysis(analysis);

            if (bean.getId() == 0) {
                this.model.insertObject(bean);
            } else {
                this.model.updateObject(bean);
            }
            PrepareNavigate prepareNavigate = new PrepareNavigate(request, model, entity);
            this.pageTo = prepareNavigate.setAttributesGetPageTo();
        } catch (ModelException ex) {
            throw new NavigatorException(ex);
        }
    }

}
