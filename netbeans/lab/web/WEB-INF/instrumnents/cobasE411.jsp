<%@ page pageEncoding="UTF-8" %>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tlds/newtag_library" prefix="sanTag"%>


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
                    <td>Sample Priority</td>
                    <td>
                        <select class="standardInputSelect" name="routineSampleOrStatSample">
                            <option value="R" ${sanTag:getSelected(order.getParamValue('routineSampleOrStatSample'),'R')}>Обычный (Routine)</option>
                            <option value="S" ${sanTag:getSelected(order.getParamValue('routineSampleOrStatSample'),'S')}>С приоритетом (Stat)</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Sample ID</td>
                    <td><input value="${order.sid}"  class="standardInputSelect" type="text" name="sid" size="20" required="required"/></td>
                </tr>
                <tr>
                    <td>Disk Number</td>
                    <td><input value="0" readonly="readonly" class="standardInputSelect" type="number" name="diskNumber" size="20" required="required"/></td>
                </tr>
                <tr>
                    <td>Position (1-30)</td>
                    <td><input value="<c:if test="${order.position != 0}">${order.position}</c:if>" class="standardInputSelect" type="number" name="position" size="20" min="1" max="30" required="required"/></td>
                </tr>
                <tr>
                    <td>Sample type</td>
                    <td>
                        <select class="standardInputSelect" name="sampleType">
                            <option value="S1" ${sanTag:getSelected(order.sampleType,'S1')}>blood serum</option>
                            <option value="S2" ${sanTag:getSelected(order.sampleType,'S2')}>urine</option>
                            <option value="S5" ${sanTag:getSelected(order.sampleType,'S5')}>others</option>
                            <option value="QC" ${sanTag:getSelected(order.sampleType,'QC')}>control sample</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Container type</td>
                    <td>
                        <select class="standardInputSelect" name="containerType">
                            <option value="SC" ${sanTag:getSelected(order.getParamValue('containerType'),'SC')}>test tube or sample cup</option>
                            <option value="MC" ${sanTag:getSelected(order.getParamValue('containerType'),'MC')}>reduced sample volume</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Dilution</td>
                    <td>
                        <select class="standardInputSelect" name="dilution">
                            <option value="1" ${sanTag:getSelected(order.getParamValue('dilution'),'1')}>None</option>
                            <option value="2" ${sanTag:getSelected(order.getParamValue('dilution'),'2')}>1:2</option>
                            <option value="5" ${sanTag:getSelected(order.getParamValue('dilution'),'5')}>1:5</option>
                            <option value="10" ${sanTag:getSelected(order.getParamValue('dilution'),'10')}>1:10</option>
                            <option value="20" ${sanTag:getSelected(order.getParamValue('dilution'),'20')}>1:20</option>
                            <option value="50" ${sanTag:getSelected(order.getParamValue('dilution'),'50')}>1:50</option>
                            <option value="100" ${sanTag:getSelected(order.getParamValue('dilution'),'100')}>1:100</option>
                        </select>
                    </td>
                </tr>

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
