var socket;
var userId;
function createConnection() {
	var wsUri = "ws://" + window.location.hostname + ":" + window.location.port
			+ "/WebSocketTest/websocket/testUpdate/";
	userId = $("#userId").val();
	$.ajax({
		url : 'userServlet',
		data : $("form").serialize(),
		success : function() {
			console.log('ajax request submitted');
			socket = new WebSocket(wsUri);
			socket.onmessage = onMessage;
			socket.onerror = onError;
			socket.onclose = onClose;
			$('form :input').attr('disabled', 'true');
		}
	});
}

function onClose(event){
	//DO Nothing
}

function closeConnection(){
	socket.close();
}

function onMessage(event) {
	var json = event.data;
	var jsonObj = JSON.parse(json);
	$("#messages").append("<span class='message'>" + jsonObj.userId + jsonObj.message + "</span>");
}

function onError(event){
	//Do nothing.
}

function submitMessage(socket, message) {
	socket.send(message);
}

function submit() {
	var message = $("#message").val();
	var jsonText = '{ "userId": "' + userId + '","message": " says ' + message + '"}'
	submitMessage(socket, jsonText);
}
