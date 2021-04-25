<h1>Lab Parameters for ${requestScope.analysis.name} </h1>

<c:if test="${not empty requestScope.message}">
    <div class ="message">${requestScope.message}</div>
</c:if>

    <b><c:out value="Кол-во параметров:"/></b>  ${listObjects.size()}<br/>
    <b><c:out value="Прибор:"/></b>  ${instrument.name}<p/>
<a href="list?entity=parameter&analysis_id=${requestScope.analysis.id}">For All instruments</a><br>
<c:forEach var="instrObj" items="${requestScope.listInstruments}"  varStatus="loopCounter">
    <a href="list?entity=parameter&analysis_id=${requestScope.analysis.id}&instrument=${instrObj.id}">For instrument ${instrObj.name}</a><br>
</c:forEach>
<table border="1" width="100%">
    <tr>
        <th>N</th>
        <th>Id</th>
        <th>Name</th>
        <th>Tests (Test default is green)</th>
        <th>-</th>
    </tr>
    <c:forEach var="object" items="${listObjects}" varStatus="loopCounter">
        <tr>
            <td>${loopCounter.count}</td>
            <td><a href="edit?entity=parameter&id=${object.id}">${object.id}</a></td>
            <td>${object.name}</td>

            <%--
           <td>${object.testDefault.code} - ${object.testDefault.name} - 
                (instrument:${object.testDefault.instrument.id})
            --%>

            <td>
                <c:forEach var="testObj" items="${object.tests}"  varStatus="loopCounter">
                    <div style = 
                         <c:choose>
                             <c:when test="${object.testDefault.id == testObj.id}">
                                 'background-color: #2ab400;'>
                             </c:when>
                             <c:otherwise>
                                 ''>
                             </c:otherwise>
                         </c:choose>
                         ${testObj.html}<br/>
                    </div>
                </c:forEach>

            </td>
            <td>---</td>
        </tr>
    </c:forEach>
</table>
<!-- Center end -->
