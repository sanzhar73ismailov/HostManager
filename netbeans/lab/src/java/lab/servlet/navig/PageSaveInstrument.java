package lab.servlet.navig;

import entity.Instrument;
import entity.InstrumentModel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lab.servlet.navig.NavigatorException;
import lab.servlet.navig.PageSaveAbstract;
import modelHost.Model;
import modelHost.ModelException;

public class PageSaveInstrument extends PageSaveAbstract {

    public PageSaveInstrument(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            this.pageTo = "listInstruments.jsp";
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String modelInstr = request.getParameter("model");
            String ip = request.getParameter("ip");
            String port = request.getParameter("port");
            String mode = request.getParameter("mode");
            String active = request.getParameter("active");
            String testMode = request.getParameter("testMode");
            Map<String, String[]> map = request.getParameterMap();
            Set<String> set = map.keySet();
            //for (Iterator<String> it = set.iterator(); it.hasNext();) {
           //     String k = it.next();
             //   System.out.println(k + "=" + map.get(k)[0]);
            //}
            Instrument bean = new Instrument();
            bean.setId(Integer.parseInt(id));
            bean.setName(name);
            bean.setModel(new InstrumentModel(Integer.parseInt(modelInstr), null));
            bean.setIp(ip);
            bean.setPort(Integer.parseInt(port));
            bean.setMode(Instrument.ModeWorking.valueOf(mode));
            bean.setActive(active != null);
            bean.setTestMode(testMode != null);

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
