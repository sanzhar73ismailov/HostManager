<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Center start -->
<h1>Add/Edit Instrument Page</h1>
${requestScope.message}
<jsp:useBean id="bean" class="entity.Parameter" scope="request"/>
<form action="save" method="post">
    <input type="hidden" name="id" value="${bean.id}"/>
    <input type="hidden" name="entity" value="parameter"/>
    <c:choose>
        <c:when test="${bean.id == 0}">
            <c:set  var="labelSaveButton" value="Save"/>
            <c:set var="labelObjectId" value="<span style='color:blue;'>New</span>"/>
        </c:when>
        <c:otherwise>
            <c:set var="labelSaveButton" value="Update"/>
            <c:set var="labelObjectId" value="${bean.id}"/>
        </c:otherwise>
    </c:choose>


    <table>
        <tr>
            <td>Order ID</td><td>${labelObjectId}</td>
        </tr>
        <tr>
            <td>Name</td>
            <td><input value="${bean.name}"  class="standardInputSelect" type="text" name="name" size="20" required="required"/></td>
        </tr>

        <tr>
            <td>Test Default</td>
            <td>
                <%--  <input value="${bean.testDefault.id}"  class="standardInputSelect" 
                         type="text" name="testId" size="20" required="required"/><br/> --%>
                <select name="testId">
                    <c:forEach var="testObj" items="${bean.tests}">
                        <option <c:if test="${bean.testDefault.id == testObj.id}">selected</c:if> value="${testObj.id}">${testObj.code} - ${testObj.name}</option>
                    </c:forEach>
                </select>
                <c:forEach var="testObj" items="${bean.tests}">
                    <p/>
                    <div style = 
                         <c:choose>
                             <c:when test="${bean.testDefault.id == testObj.id}">
                                 'background-color: #2ab400;'>
                             </c:when>
                             <c:otherwise>
                                 ''>
                             </c:otherwise>
                         </c:choose>
                         <c:out value="${testObj.html}" escapeXml="false"/>
                    </div>
                </c:forEach>
                    
                    <select name="analysisId">
                        <c:forEach var="analObj" items="${requestScope.analyses}">
                        <option <c:if test="${bean.analysis.id == analObj.id}">selected</c:if> value="${analObj.id}">${analObj.name}</option>
                    </c:forEach>
                </select>

            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input class="buttonGeneral" onclick="return checkIfLessOneTestSelected();" 
                       type="submit" name="save" value="${labelSaveButton}" size="20"/>
                <input class="buttonGeneral" type="reset" name="reset" value="Сброс" size="20"/></td>
        </tr>
    </table>

</form>
<!-- Center end -->

<script type="text/javascript">
</script>
</body>
</html>
