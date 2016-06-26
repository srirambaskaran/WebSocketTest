var socket;
var userId;
function createConnection() {
	userId = $("#userId").val();
	if(userId === ""){
		alert("Enter Username")
	}else{
		$("#message").keypress(function(e) {
		    if(e.which == 13) {
		        submit();
		    }
		});
		$("#message").focus();
		var wsUri = "ws://" + window.location.hostname + ":" + window.location.port
		+ "/BroadcastChat/websocket/testUpdate/";
		$.ajax({
			url : 'userServlet',
			method: 'post',
			data : $("form").serialize(),
			success : function() {
				console.log('ajax request submitted');
				socket = new WebSocket(wsUri);
				socket.onmessage = onMessage;
				socket.onerror = onError;
				socket.onclose = onClose;
				$('form :input').attr('disabled', 'true');
			},
			error: function(){
				alert("Error during login");
			}
		});
	}
}

function onClose(event){
	closeConnection();
	$("#messages").append("<span class='message user-leave-message'>" + jsonObj.userId+ ":" + jsonObj.message + "</span>");
}

function closeConnection(){
	socket.close();
	$('form #userId').removeAttr('disabled');
	$('form input[type="button"]').removeAttr('disabled');
	$('form #userId').focus();
}

function onMessage(event) {
	var json = event.data;
	var jsonObj = JSON.parse(json);
	$("#messages").append("<span class='message'>" + jsonObj.userId+ ":" + jsonObj.message + "</span>");
	var height = 0;
	$('#messages .message').each(function(i, value){
	    height += parseInt($(this).height());
	});

	height += '';

	$('#messages').animate({scrollTop: height});
}

function onError(event){
	//Do nothing.
}

function submitMessage(socket, message) {
	socket.send(message);
}

function submit() {
	var message = $("#message").val();

	if(userId === ""){
		alert("No User Logged In")
	}else{
		if(message === ""){
			alert("Enter Message")
		}else{
			var jsonText = '{ "userId": "' + userId + '","message": "' + message + '"}'
			$("#message").val("");
			$("#message").focus();
			submitMessage(socket, jsonText);
		}
	}
}
