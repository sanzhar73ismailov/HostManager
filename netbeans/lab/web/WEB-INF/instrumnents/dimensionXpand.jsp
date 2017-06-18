<%@ page pageEncoding="UTF-8" %>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tlds/newtag_library" prefix="sanTag"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<table>
    <tr>
        <td>
            <table>
                <tr>
                    <td>Order ID</td><td>${param.labelObjectId}</td>
                </tr>
                <tr>
                    <td>Message ID</td>
                    <td><input readonly="readonly" value="1"  class="standardInputSelect" type="text" name="mid" size="20"/></td>
                </tr>

                <tr>
                    <td>Sample ID</td>
                    <td><input value="${order.sid}"  class="standardInputSelect" type="text" name="sid" size="20" required="required"/></td>
                </tr>
                <tr>
                    <td>Sample type</td>
                    <td>
                        <select class="standardInputSelect" name="sampleType">
                            <option value="ser" ${sanTag:getSelected(order.sampleType,'ser')}>Serum</option>
                            <option value="wb" ${sanTag:getSelected(order.sampleType,'wb')}>whole blood</option>
                            <option value="pl" ${sanTag:getSelected(order.sampleType,'pl')}>Plasma</option>
                            <option value="urine" ${sanTag:getSelected(order.sampleType,'urine')}>urine</option>
                            <option value="csf" ${sanTag:getSelected(order.sampleType,'csf')}>Cerebral Spinal Fluid</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Patient name</td>
                    <td><input value="${order.patientName}" class="standardInputSelect" 
                               type="text" name="patientName" size="20"/></td>
                </tr>
                <td>&nbsp;</td>
                <td><input class="buttonGeneral" onclick="return checkIfLessOneTestSelected();" 
                           type="submit" name="save" value="${param.labelSaveButton}" size="20"/>
                    <input class="buttonGeneral" type="reset" name="reset" value="Сброс" size="20"/></td>
    </tr>
</table>
</td>
<td>
    <fieldset>
        <legend>Tests</legend>
        <table cellpadding="4px">
            <tr>
                <c:forEach var="testElem" items="${dicTests}" varStatus="loopCounter">
                    <td><div class="cellForTest"><input class="testCheckBox" type="checkbox" name="tests" 
                                                        value="${testElem.code}"/>${testElem.name}</div></td>
                    <c:if test="${(loopCounter.count % 6) == 0}"></tr><tr></c:if>
                </c:forEach>
            </tr>
        </table>
    </fieldset>
</td>
</table>
