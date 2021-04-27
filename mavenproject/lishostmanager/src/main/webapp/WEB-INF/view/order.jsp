
                    <!-- Center start -->
                    <h1>Add/Edit Order Page</h1>
                    ${requestScope.message}
                    <jsp:useBean id="order" class="kz.biostat.lishostmanager.comport.entity.WorkOrder" scope="request"/>
                    <h1> Instrument: ${requestScope.instrument.name}</h1>
                    <form action="save" method="post">
                        <input type="hidden" name="id" value="${order.id}"/>
                        <input type="hidden" name="entity" value="order"/>
                        <input type="hidden" name="instrument" value="${order.instrument.id}"/>
                        <input type="hidden"  id="testsForJavaScript" value="${order.tests}"/>
                        <c:choose>
                            <c:when test="${order.id == 0}">
                                <c:set  var="labelSaveButton" value="Save"/>
                                <c:set var="labelObjectId" value="<span style='color:blue;'>New</span>"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="labelSaveButton" value="Update"/>
                                <c:set var="labelObjectId" value="${order.id}"/>
                            </c:otherwise>
                        </c:choose>
                        <jsp:include page="../instrumnents/${requestScope.formName}.jsp">
                            <jsp:param name="labelSaveButton" value="${labelSaveButton}"/>
                            <jsp:param name="labelObjectId" value="${labelObjectId}"/>
                        </jsp:include>
                        
                    </form>
                    <!-- Center end -->
                
       <script type="text/javascript">
            function checkIfLessOneTestSelected() {
                var inputs = document.getElementsByName("tests");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].checked) {
                        return true;
                    }
                }
                alert("Необходимо выбрать как минимум один тест!");
                return false;
            }
            function selectTest(obj) {
                var divElement = obj.parentNode;//document.getElementById("div" + obj.value);
                if (obj.checked) {
                    divElement.style.backgroundColor = "yellow";
                } else {
                    divElement.style.backgroundColor = "#cee2d3";
                }
            }
            function selectTestFromDiv(obj) {
                var inputCheckBox = obj.firstElementChild;
                if (inputCheckBox.checked) {
                    inputCheckBox.checked = false;
                } else {
                    inputCheckBox.checked = true;
                }
                selectTest(inputCheckBox);

            }
            function contains(a, obj) {
                for (var i = 0; i < a.length; i++) {
                    if (a[i] === obj) {
                        return true;
                    }
                }
                return false;
            }
            window.onload = function() {
                try {
                    var inputs = document.getElementsByName("tests");
                    var testsForJavaScript = document.getElementById("testsForJavaScript");
                    if (testsForJavaScript.value != "") {
                        var testArray = testsForJavaScript.value.split(",");
                        for (var i = 0; i < inputs.length; i++) {
                            //var valAsInt = parseInt(inputs[i].value);
                            if (contains(testArray, inputs[i].value)) {
                                inputs[i].checked = true;
                                selectTest(inputs[i]);
                            }
                        }

                    }
                    /*
                     for (var i = 0; i < inputs.length; i++) {
                     inputs[i].onclick = function() {
                     selectTest(this);  // внутри автосозданной функции
                     }
                     }
                     */

                    inputs = document.getElementsByClassName("cellForTest");
                    for (var i = 0; i < inputs.length; i++) {
                        inputs[i].onclick = function() {
                            selectTestFromDiv(this);  // внутри автосозданной функции
                        }
                    }
                } catch (e) {
                    alert(e.message + " в " + e.fileName + ", строка " + e.lineNumber);
                }

            }
        </script>
    </body>
</html>
