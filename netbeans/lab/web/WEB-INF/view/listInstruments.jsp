<h1>Lab Instruments </h1>
<a href="edit?entity=instrument"><button class="buttonGeneral">Add new</button></a>

<a href="apparatus?action=startAllActive"><button class="buttonGeneral">Start all active</button></a>
<a href="list?entity=instrument"><button class="buttonGeneral" style="color: blue">Refresh</button></a>
<c:if test="${not empty requestScope.message}">
    <div class ="message">${requestScope.message}</div>
</c:if>
<table border="1" width="100%">
    <tr>
        <th>Nickname</th>
        <th>Model</th>
        <th>IP (port)</th>
        <th>Mode</th>
        <th>Active</th>
        <th>Test Mode</th>
        <th>-</th>
        <th>-</th>
        <th>-</th>
        <th>-</th>
        <th>-</th>
        <th>Status now</th>
    </tr>
    <c:forEach var="instr" items="${listInstruments}">
        <tr>
            <td><a href="edit?entity=instrument&id=${instr.id}">${instr.name}</a></td>
            <td>${instr.model.name} - ${instr.model.type} system</td>
            <td>${instr.ip} (${instr.port})</td>
            <td>${instr.mode}</td>
            <td>${instr.active}</td>
            <td>${instr.testMode}</td>
            <td><a href="edit?entity=order&instrument=${instr.id}">Add Order</a></td>
            <td><a href="list?entity=order&instrument=${instr.id}">List of Orders</a></td>
            <td><a href="list?entity=log&instrument=${instr.id}">Logs</a></td>
            <td><a href="apparatus?action=start&instrument=${instr.id}">Start</a></td>
            <td><a href="apparatus?action=stop&instrument=${instr.id}">Stop</a></td>
            <%-- 
              <td><a href="remove?entity=instrument&id=${instr.id}">remove</a></td>
            --%>
            <td>
                <c:choose>
                    <c:when test="${instr.runNow}">
                        <span style="background-color: green">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>    
                    </c:when>
                    <c:otherwise>
                        <span style="background-color: gray">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>    
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>
<!-- Center end -->
