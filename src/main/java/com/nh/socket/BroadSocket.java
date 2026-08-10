package com.nh.socket;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class BroadSocket extends TextWebSocketHandler {
	public static Map<String, Set<WebSocketSession>> clientsMap = new HashMap<>();
	public static Set<String> aiLock = Collections.synchronizedSet(new HashSet<>());
	
	Set<WebSocketSession> getClients(WebSocketSession session) {
		String query = session.getUri().getQuery();
		String key = query.substring(query.indexOf("=")+1);
		System.out.println("웹소켓 요청 발생, key:" + key);
		
		if(clientsMap.containsKey(key)) return clientsMap.get(key);
		Set<WebSocketSession> clients = Collections.synchronizedSet(new HashSet<>());
		clientsMap.put(key, clients);
		return clients;
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Set<WebSocketSession> clients = getClients(session);
		clients.add(session);
		//System.out.println("새로운 클라이언트: 현재 " + clients.size() + "명");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws Exception {
		Set<WebSocketSession> clients = getClients(session);
		clients.add(session);
		//System.out.println("클라이언트로 부터 도착한 메세지: " + msg.getPayload());
		TextMessage tm = null;
		String query = session.getUri().getQuery();
		String key = query.substring(query.indexOf("=")+1);
		if("ai".equals(msg.getPayload())) {
			if(aiLock.contains(key))
				tm = new TextMessage("true");
			else {
				tm = new TextMessage("false");
				aiLock.add(key);
			}
			
			for(WebSocketSession client : clients) {
				if(session == client) {
					//client.getBasicRemote().sendText(msg);
					client.sendMessage(tm);
					return;
				}
			}
		} else {
			if("aiUpdate".equals(msg.getPayload())) aiLock.remove(key);
			tm = new TextMessage(msg.getPayload());
		}
		for(WebSocketSession client : clients) {
			if(session != client) {
				//client.getBasicRemote().sendText(msg);
				client.sendMessage(tm);
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Set<WebSocketSession> clients = getClients(session);
		clients.add(session);
		if (clients != null) {
			clients.remove(session);
			if (clients.isEmpty()) {
				String query = session.getUri().getQuery();
				String key = query.substring(query.indexOf("=")+1);
				clientsMap.remove(key);
				aiLock.remove(key);
			}
		}
		//System.out.println("클라이언트 나감: 현재 " + clients.size() + "명");
	}
}
