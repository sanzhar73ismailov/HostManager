package lab.servlet.navig;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelHost.Model;

public class PageIndex extends AbstractPageNavigator {

    public PageIndex(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    public PageIndex() throws NavigatorException {
        super(null, null, null);
    }

    @Override
    public void doSomething() throws NavigatorException {
        pageTo = "index.jsp";
        String manifestVersion = "unknown version";
        Properties prop = new Properties();
        try {
            prop.load(request.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
        } catch (IOException ex) {
            Logger.getLogger(PageIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        manifestVersion = prop.getProperty("Manifest-Version");
        request.setAttribute("manifestVersion", manifestVersion);
    }

}
