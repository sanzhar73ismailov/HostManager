<!-- Center start -->
<h1>List of work orders</h1>
<h1>Apparatus ${requestScope.instrument.name}</h1>

<a href="deleteAll?instrument=${requestScope.instrument.id}" onclick="return  confirm('Удалить все задания?')"><button class="buttonGeneral">Remove all</button></a>
<a href="edit?entity=order&instrument=${requestScope.instrument.id}&positionLast=${order.position}"><button class="buttonGeneral">Add new</button></a>
<a href="list?entity=order&instrument=${requestScope.instrument.id}"><button class="buttonGeneral"  style="color: blue">Refresh</button></a>


<c:if test="${not empty requestScope.message}">
    <div class ="message">${requestScope.message}</div>
</c:if>

<table border="1" width="100%" cellpadding="5" cellspacing="0" 
       style=" margin: 5px ;border-color: gray; border-collapse:collapse; table-layout: fixed;">
    <tr style="font-size: medium">
        <th>ID</th>
        <th>Instrument</th>
        <th>Message ID</th>
        <th>Patient</th>
        <th>Sample Status</th>
        <th>Sample ID</th>
        <th>Disk Number</th>
        <th>Position</th>
        <th>Sample type</th>
        <th>Container type</th>
        <th>Dilution</th>
        <th style="width: 80px;">Tests</th>
        <th>Status</th>
        <th>-</th>
    </tr>

    <c:forEach var="order" items="${listWorkOrders}"  varStatus="loopCounter" >
        <c:choose>
            <c:when test="${order.id == requestScope.order.id}">
                <c:set var="color" value="red"/>
            </c:when>
            <c:otherwise>
                <c:set var="color" value="inherit"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${order.status == 0}">
                <c:set var="status" value="1"/>
                <c:set var="background" value="green"/>
            </c:when>
            <c:otherwise>
                <c:set var="status" value="0"/>
                <c:set var="background" value="grey"/>
            </c:otherwise>
        </c:choose>
        <tr>
            <td>
                <a href="edit?entity=order&id=${order.id}&instrument=${param.instrument}">${order.id}</a>
                <%-- 
                <c:choose>
                    <c:when test="${order.instrument eq 'cobas411'}">
                        <a href="editOrder?id=${order.id}">${order.id}</a>
                    </c:when>
                    <c:otherwise>
                        ${order.id}
                    </c:otherwise>
                </c:choose>
                --%>
                <div style="background-color: ${background}; border-radius: 2px; width: 30px; height: 20px; display: inline-block; vertical-align:bottom;"></div>
            </td>
            <td style="color: ${color};">${order.instrument.name}</td>
            <td>${order.mid}</td>
            <td>PID: ${order.patientNumber}<br/>
                PName: ${order.patientName}<br/>
                PSex ${order.sex} <br/>
                Collect Date: ${order.dateCollection}<br/>
                InsertDateTime: ${order.insertDatetime}<br/>
                
            </td>
            <td>${sanTagLibrary:getWorkOrderProperty(order,"routineSampleOrStatSample")}</td>
            <td>${order.sid}</td>
            <td>${order.rack}</td>
            <td>${order.position}</td>
            <td>${order.sampleType}</td>
            <td>${sanTagLibrary:getWorkOrderProperty(order,"containerType")}</td>
            <td>${sanTagLibrary:getWorkOrderProperty(order,"dilution")}</td>
            <td style="font-size: smaller;width: 60px; word-wrap: normal;">

                <c:forTokens items="${order.tests}" delims="," var="testKey">

                    ${sanTagLibrary:getValueFromDic(requestScope.dicTests, testKey)}<br/>

                </c:forTokens>
            </td>

            <td>
                <a href="changeStatus?id=${order.id}&status=${status}&instrument=${param.instrument}">change status</a>
            </td>
            <td>
                <a href="remove?entity=order&id=${order.id}&instrument=${param.instrument}">remove</a>
            </td>
        </tr>

    </c:forEach>
</table>

<c:forEach var="par" items="${requestScope.mapParams}">
    <c:choose>
        <c:when test="${par.key == 'tests'}"> 
            tests777 - ${par.key} - ${par.value}<br/>
            <c:forEach var="testCode" items="${par.value}">
                ${testCode}<br>
            </c:forEach>

        </c:when>
        <c:otherwise>
            ${par.key} - ${par.value[0]}<br/>
        </c:otherwise>
    </c:choose> 




</c:forEach>
${requestScope.sessionInfo}

<c:forEach var="order" items="${sessionScope.listWorkOrders}">
    ${order}<br>
</c:forEach>

<!-- Center end -->
