<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Center start -->
<h1>Add/Edit Instrument Page</h1>
${requestScope.message}
<jsp:useBean id="bean" class="kz.biostat.lishostmanager.comport.entity.Instrument" scope="request"/>
<form action="save" method="post">
    <input type="hidden" name="id" value="${bean.id}"/>
    <input type="hidden" name="entity" value="instrument"/>
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
            <td>Model</td>
            <td>
                <select class="standardInputSelect" name="model">
                    <c:forEach var="model" items="${modelList}">
                    <option value="${model.id}" <c:if test="${bean.model.id == model.id}">selected</c:if>>${model.name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>IP</td>
            <td><input value="${bean.ip}"  class="standardInputSelect" type="text" name="ip" size="20" required="required"/></td>
        </tr>
        <tr>
            <td>Port</td>
            <td><input value="${bean.port}"  class="standardInputSelect" type="number" name="port" size="20" required="required"/></td>
        </tr>
        <tr>
            <td>Mode working</td>
            <td>
                <select class="standardInputSelect" name="mode">
                    <option value="BATCH" <c:if test="${bean.mode == 'BATCH'}">selected</c:if>>Batch</option>
                    <option value="QUERY" <c:if test="${bean.mode == 'QUERY'}">selected</c:if>>Query</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>Active</td>
            <td>
                <input type="checkbox" name="active" <c:if test="${bean.active}">checked="checked"</c:if>/>
            </td>
        </tr>
        <tr>
            <td>Test mode</td>
            <td>
                <input type="checkbox" name="testMode" <c:if test="${bean.testMode}">checked="checked"</c:if>/>
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
