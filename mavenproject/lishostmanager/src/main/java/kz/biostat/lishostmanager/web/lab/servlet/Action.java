package kz.biostat.lishostmanager.web.lab.servlet;

import kz.biostat.lishostmanager.web.lab.servlet.navig.AbstractPageNavigator;
import kz.biostat.lishostmanager.web.lab.servlet.navig.NavigatorException;

import java.io.IOException;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;


public class Action extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        if (1 == 0) {
            // for testing
            System.out.println("                     ---");
            System.out.println("    <<<<<<<<<<<<<<");
            System.out.println("request.getCharacterEncoding(): " + request.getCharacterEncoding());
            System.out.println("response.getContentType(): " + response.getContentType());
            System.out.println("response.getCharacterEncoding(): " + response.getCharacterEncoding());
            Map<String, String[]> map = request.getParameterMap();
            System.out.println("!!!!!!!!!!!! -------- Map of init params: ");
            Set<String> keys = map.keySet();
            System.out.println("keys: " + keys.size());
            int i = 0;
            for (String key : keys) {
                i++;
                System.out.println(i + ") key " + key + "=" + Arrays.toString(map.get(key)));
            }
            System.out.println("   >>>>>>>>>>>>>>>>>>");
            System.out.println("   >>>>>>>>>>>>>>>>>>");
            System.out.println("path = " + path);
        }

        AbstractPageNavigator pageNavigator = null;
        try {
            pageNavigator = FabricaPageNavigator.createPageNavigator(request, response);
        } catch (NavigatorException ex) {
            System.out.println("ex = " + ex.getMessage());
            ex.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(pageNavigator.getPageTo());
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
