<%-- 
    Document   : sql
    Created on : Aug 10, 2015, 11:49:56 AM
    Author     : sanzhar.ismailov
--%>

<%@page import="kz.biostat.lishostmanager.comport.modelHost.SqlResult"%>
<%@page import="kz.biostat.lishostmanager.comport.modelHost.SqlNative"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${pageTitle}</title>
    </head>
    <body>
        <h1>SQL query builder-${sessionScope.showSqlForm}</h1>
        <div style="color: red">${message}</div>
        <c:choose>
            <c:when test="${sessionScope.showSqlForm}">

                <form action="sql" method="post">
                    <textarea name="query" cols="80" rows="10">${query}</textarea>
                    <br/>
                    <input type="submit" name="go" value="Run!"/>
                </form>

                <h2>${sqlResult.resultMessage}</h2>
                <%
                    //SqlNative sqlNative = SqlNative.getInstance();
                    //SqlResult sqlResult = sqlNative.executeQuery("");
                    if (request.getAttribute("sqlResult") != null) {
                        SqlResult sqlResult = (SqlResult) request.getAttribute("sqlResult");
                        String[] columnNames = sqlResult.getColumns();
                        String[][] dataTable = sqlResult.getDataTable();

                        out.println("<table border='1' style='border-collapse:collapse;'>");
                        for (int i = 0; i < columnNames.length; i++) {
                            if (i == 0) {
                                out.println("<td>RowNum</td>");
                            }
                            out.println("<td><b>" + columnNames[i] + "</b></td>");
                        }
                        for (int i = 0; i < dataTable.length; i++) {
                            String[] row = dataTable[i];
                            out.println("<tr>");
                            // if(i==0){
                            out.println("<td style='color:blue'>" + (i + 1) + "</td>");
                            // }
                            for (String cell : row) {
                                out.println("<td>" + cell + "</td>");
                            }
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }
                %>   
            </c:when>
            <c:otherwise>
                <form action="sql" method="post">
                    password: <input type="password" name="paramShowSqlForm"/> 
                    <input type="submit" value="Go"/>
                </form>
            </c:otherwise>
        </c:choose>



    </body>
</html>
