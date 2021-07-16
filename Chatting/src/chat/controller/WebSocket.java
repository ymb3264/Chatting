package chat.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import chat.model.service.ChatService;

@ServerEndpoint("/websocket")
public class WebSocket {
	
	private static List<Session> sessionUsers = Collections.synchronizedList(new ArrayList<>());
	
	@OnOpen
	public void handleOpen(Session userSession) {
		sessionUsers.add(userSession);
		
		System.out.println("...");
	}
	
	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
//		System.out.println(message);
		String[] msgList = message.split("/");
		String roomId = msgList[0];
		String user1 = msgList[1];
		String user2 = msgList[2];
		String msg = msgList[3];
		
		int result = new ChatService().insertMessage(roomId, user1, msg);
		
		if(result > 0) {
			sessionUsers.forEach(session -> {
				if (session == userSession) {
					return;
				}
				try {
					session.getBasicRemote().sendText(msgList[0] + "/" + msgList[1] + "/" + msgList[3]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
	}
	
	@OnClose
	public void handleClose(Session userSession) {
		sessionUsers.remove(userSession);
		System.out.println("end...");
	}
}
