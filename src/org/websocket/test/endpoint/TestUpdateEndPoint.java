package org.websocket.test.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;
import org.websocket.test.endpoint.utils.EndPointConfigurator;
import org.websocket.test.endpoint.utils.JSONDecoder;
import org.websocket.test.endpoint.utils.JSONEncoder;

@ServerEndpoint(value = "/websocket/testUpdate/",
	encoders = JSONEncoder.class,
	decoders = JSONDecoder.class, 
	configurator = EndPointConfigurator.class)
public class TestUpdateEndPoint {

	private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) throws IOException, JSONException, EncodeException {
		
		sessions.add(session);
		String userId = (String) config.getUserProperties().get("userId");
		sendConnection(userId);
	}

	@OnClose
	public void onClose(Session session) throws IOException, JSONException, EncodeException {
		sessions.remove(session);
		String userId = (String) session.getUserProperties().get("userId");
		sendDisconnection(userId);

	}
	
	@OnError
	public void onError(Session session, Throwable t){
		//Do nothing.
	}

	@OnMessage
	public void onMessage(JSONObject json) throws IOException,
			EncodeException {
		sendMessageToAll(json);
	}

	public void sendMessageToAll(JSONObject json) throws IOException,
			EncodeException {
		for (Session s : sessions) {
			synchronized (s) {
				s.getBasicRemote().sendObject(json);				
			}
		}
	}

	public void sendConnection(String userId) throws IOException, JSONException, EncodeException {
		for (Session s : sessions) {
			synchronized (s) {
				s.getBasicRemote().sendObject(new JSONObject("{ \"userId\" : \""+userId+"\", \"message\" : \" has joined the chat\"}"));
			}
		}
	}

	public void sendDisconnection(String userId) throws IOException, JSONException, EncodeException {
		for (Session s : sessions) {
			synchronized (s) {
				s.getBasicRemote().sendObject(new JSONObject("{ \"userId\" : \""+userId+"\", \"message\" : \" has left the chat\"}"));
			}
		}
	}
}
