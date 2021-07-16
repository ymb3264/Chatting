package chat.model.service;

import static common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.ArrayList;

import chat.model.dao.ChatDAO;
import chat.model.vo.ChatRoom;
import chat.model.vo.Message;

public class ChatService {

	public String getUserId(String id) {
		Connection conn = getConnection();
		
		String userId = new ChatDAO().getUserId(conn, id);
		
		close(conn);
		
		return userId;
	}

	public ArrayList<String> selectUser() {
		Connection conn = getConnection();
		
		ArrayList<String> userList = new ChatDAO().selectUser(conn);
		
		close(conn);
		
		return userList;
	}

	public ChatRoom createRoom(ChatRoom cr) {
		Connection conn = getConnection();
		
		ChatRoom room = new ChatDAO().selectChatRoom(conn, cr);
		
		if(room == null) {
			int result = new ChatDAO().createRoom(conn, cr);
			
			if(result > 0) {
				commit(conn);
				room = new ChatDAO().selectChatRoom(conn, cr);
			} else {
				rollback(conn);
			}
		}
		
		close(conn);
		
		return room;
	}

	public ArrayList<Message> selectMessage(ChatRoom room) {
		Connection conn = getConnection();
		
		ArrayList<Message> msgList = new ChatDAO().selectMessage(conn, room);
		
		close(conn);
		
		return msgList;
	}

	public int insertMessage(String roomId, String user1, String msg) {
		Connection conn = getConnection();
		
		int result = new ChatDAO().insertMessage(conn, roomId, user1, msg);
		
		close(conn);
		
		return result;
	}

	public ArrayList<ChatRoom> selectMyChatRoom(String userId) {
		Connection conn = getConnection();
		
		ArrayList<ChatRoom> roomList = new ChatDAO().selectMyChatRoom(conn, userId);
		
		close(conn);
		
		return roomList;
	}
	
}
