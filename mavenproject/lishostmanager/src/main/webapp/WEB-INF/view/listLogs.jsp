<%@page import="kz.biostat.lishostmanager.comport.modelHost.ModelImpl"%>
<!-- Center start -->
<h1>List of logs <span style="color:red;">${autoRefreshMessage}</span></h1>
<h1>Instrument ${instrument.name} (Id: ${param.instrument})</h1> 

<table border="1" style="border-collapse: collapse;">
    <tr>
        <td><a href="list?entity=log&instrument=${param.instrument}">Show All</a></td>
        <td><a href="list?entity=log&instrument=${param.instrument}&autoRefresh=1">Show All (Auto refresh)</a></td>
        <td><a href="list?entity=log&instrument=${param.instrument}&temp=off">Show without temp</a></td>
        <td><a href="list?entity=log&instrument=${param.instrument}&deleteTemp=1">Temp logs delete</a></td>
        
    </tr>

</table>
        

<c:if test="${not empty requestScope.message}">
    <div class ="message">${requestScope.message}</div>
</c:if>

<table border="1" cellpadding="5" cellspacing="0" 
       style=" margin: 5px ;border-color: gray; border-collapse:collapse; table-layout: fixed;  width:100%">
    <col width="5%" valign="top">
    <col width="5%" valign="top">
    <col width="10%" valign="top">
    <col width="70%" valign="top">
    <col width="10%" valign="top">
    <tr style="font-size: medium">
        <th>N</th>
        <th>ID</th>
        <th>Direction</th>
        <th>Message</th>
        <th>Date</th>
    </tr>

    <c:forEach var="obj" items="${listLogs}"  varStatus="loopCounter" >
        <tr style="background-color: white;">
            <td>${loopCounter.count}</td>
            <c:choose>
                <c:when test="${obj.temp}">
                    <td style="color:blueviolet;">${obj.id}</td>
                </c:when>
                <c:otherwise>
                    <td>${obj.id}</td>
                </c:otherwise>
            </c:choose>
            <td>${obj.directionForHTML}</td>
            <td  id="tdMessage${obj.id}" style="word-wrap:break-word;">
               <%-- ${obj.messageForHTML} --%>
                
                <c:choose>
                    <c:when test="${obj.messageTooBig}">
                        <div id="shortMess_${obj.id}">${obj.shortMessageForHTML}<br>
                             <a href="" id="linkDetail_${obj.id}" onclick="showDetailsOfMessage(${obj.id});return false;">Show details</a>
                        </div>
                        <div style="display:none;" id="detailtMess_${obj.id}">
                            <a href="" onclick="showShortMessage(${obj.id});return false;">Hide details</a><br/>
                            ${obj.messageForHTML}</div>
                    </c:when>
                    <c:otherwise>
                        ${obj.messageForHTML}
                    </c:otherwise>
                </c:choose>
                
                <%-- <c:out value="${obj.messageForHTML}"/> --%>
            </td>
            <td><fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm:ss"  value="${obj.insertDate}" /></td>
        </tr>
    </c:forEach>
</table>
<!-- Center end -->