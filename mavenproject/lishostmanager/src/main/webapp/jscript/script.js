function testAlert(){
    alert(123123);    
}
function showDetailsOfMessage(id){
    var detailtMessElement = document.getElementById("detailtMess_" + id);
    var shortMessElement = document.getElementById("shortMess_" + id);
    detailtMessElement.style.display = "block";
    shortMessElement.style.display = "none";
}

function showShortMessage(id){
    var detailtMessElement = document.getElementById("detailtMess_" + id);
    var shortMessElement = document.getElementById("shortMess_" + id);
    detailtMessElement.style.display = "none";
    shortMessElement.style.display = "block";
}


