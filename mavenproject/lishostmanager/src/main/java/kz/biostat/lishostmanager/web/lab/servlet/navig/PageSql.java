package kz.biostat.lishostmanager.web.lab.servlet.navig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.SqlNative;
import kz.biostat.lishostmanager.comport.modelHost.SqlResult;

public class PageSql extends AbstractPageNavigator {

    public PageSql(HttpServletRequest request, HttpServletResponse response, Model model) throws NavigatorException {
        super(request, response, model);
    }

    @Override
    public void doSomething() throws NavigatorException {
        try {
            PrepareNavigate prepareNavigate = new PrepareNavigate(request, model, "sql");
            this.pageTo = prepareNavigate.setAttributesGetPageTo();
            boolean showSqlForm = false;

            String paramSqlForm = null;
            paramSqlForm = request.getParameter("paramShowSqlForm");

            //System.out.println("paramSqlForm = " + paramSqlForm);
            HttpSession session = request.getSession();
            if (session.getAttribute("showSqlForm") == null) {
                if (paramSqlForm != null && !paramSqlForm.trim().isEmpty()) {
                    if (paramSqlForm.trim().equals("qwerty")) {
                        showSqlForm = true;
                    }
                }
            } else {
                Boolean attShowSqlForm = (Boolean) session.getAttribute("showSqlForm");
                if (attShowSqlForm) {
                    showSqlForm = true;
                } else {
                    if (paramSqlForm != null && !paramSqlForm.trim().isEmpty()) {
                        if (paramSqlForm.trim().equals("qwerty")) {
                            showSqlForm = true;
                        }
                    }
                }
            }
            session.setAttribute("showSqlForm", showSqlForm);
            if (showSqlForm) {

                String paramQuery = request.getParameter("query");

                if (paramQuery != null && !paramQuery.trim().isEmpty()) {
                    paramQuery = paramQuery.trim();
                    request.setAttribute("query", paramQuery);
                    if (!paramQuery.isEmpty()) {
                        SqlNative sqlNative = SqlNative.getInstance();
                        SqlResult sqlResult = sqlNative.executeQuery(paramQuery);
                        request.setAttribute("sqlResult", sqlResult);
                    }
                }
            }

        } catch (Exception ex) {
            request.setAttribute("message", ex.getMessage());
        }
    }

}
