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
                    <td>Sample Priority (Stat indicator)</td>
                    <td>
                        <select class="standardInputSelect" name="routineSampleOrStatSample">
                            <option value="R" ${sanTag:getSelected(order.getParamValue('routineSampleOrStatSample'),'R')}>Обычный (Routine)</option>
                            <option value="S" ${sanTag:getSelected(order.getParamValue('routineSampleOrStatSample'),'S')}>С приоритетом (Stat)</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>Update indicator</td>
                    <td>
                        <select class="standardInputSelect" name="updateIndicator">
                            <option value="R" ${sanTag:getSelected(order.getParamValue('updateIndicator'),'no_update')}>New Workorder (No update)</option>
                            <option value="S" ${sanTag:getSelected(order.getParamValue('updateIndicator'),'update')}>Update  Workorder </option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>Sample ID</td>
                    <td><input value="${order.sid}"  class="standardInputSelect" type="text" name="sid" size="20" required="required"/></td>
                </tr>
                <%--
                <tr>
                    <td>Rack Number (1-15)</td>
                    <td><input value="${order.rack}" class="standardInputSelect" 
                               type="number" name="rack" size="20" min="1" max="15"  required="required"/></td>
                </tr>
                <tr>
                    <td>Position (1-10)</td>
                    <td><input value="<c:if test="${order.position != 0}">${order.position}</c:if>" 
                               class="standardInputSelect" type="number" name="position" 
                               size="20" min="1" max="10" required="required"/></td>
                    </tr>
                --%>

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
                    <td><input value="${order.dateBirth}"  class="standardInputSelect" 
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
                    <td><input value="${order.dateCollection}"  class="standardInputSelect" 
                               type="text" name="dateCollection" size="20" placeholder="dd/mm/yyyy"/></td>
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
            <span id="standartSet" style="display: none;">1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,42,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,72,73,83,84,191,192</span>
            <button onclick="selectStandartSet();return false;">Select standart set </button>

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
<script type="text/javascript">
    function selectStandartSet() {
        var el = document.getElementById("standartSet");
        var testArray = el.innerHTML.split(",");
        var inputs = document.getElementsByName("tests");
        for (var i = 0; i < inputs.length; i++) {
            if (contains(testArray, inputs[i].value)) {
                inputs[i].checked = true;
                selectTest(inputs[i]);
            }
        }
    }
</script>