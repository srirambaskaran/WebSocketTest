package org.websocket.test.endpoint.utils;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class EndPointConfigurator extends ServerEndpointConfig.Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		HttpSession httpSession = (HttpSession)request.getHttpSession();
		sec.getUserProperties().put("userId",httpSession.getAttribute("userId"));
		super.modifyHandshake(sec, request, response);
	}

}
