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
                    <td>Patient name</td>
                    <td><input value="${order.patientName}" class="standardInputSelect" 
                               type="text" name="patientName" size="20"/></td>
                </tr>

                <tr>
                    <td>Patient ID</td>
                    <td><input value="${order.patientNumber}"  class="standardInputSelect" 
                               type="text" name="patientNumber" size="20"/></td>
                </tr>

                <tr>
                    <td>Patient DOB</td>
                    <td><input value='<fmt:formatDate pattern="dd/MM/yyyy" value="${order.dateBirth}"/>'  class="standardInputSelect" 
                               type="text" name="dateBirth" size="20" placeholder="dd/mm/yyyy"/></td>

                </tr>

                <tr>
                    <td>Patient sex</td>
                    <td>
                        <select class="standardInputSelect" name="sex">
                            <option value="1" ${sanTag:getSelected(order.sex,'1')}>male</option>
                            <option value="2" ${sanTag:getSelected(order.sex,'2')}>female</option>
                            <option value="3" ${sanTag:getSelected(order.sex,'3')}>unknown</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>Date collection</td>
                    <td><input value='<fmt:formatDate pattern="dd/MM/yyyy" value="${order.dateCollection}"/>'  class="standardInputSelect" 
                               type="text" name="dateCollection" size="20" placeholder="dd/mm/yyyy"/></td>
                </tr>F

                <tr>
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
