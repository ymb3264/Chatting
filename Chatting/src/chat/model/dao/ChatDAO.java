package chat.model.dao;

import static common.JDBCTemplate.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import chat.model.vo.ChatRoom;
import chat.model.vo.Message;

public class ChatDAO {
	
	private Properties prop = new Properties();

	public ChatDAO() {
		String fileName = ChatDAO.class.getResource("/sql/driver.properties").getPath();
		
		try {
			prop.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUserId(Connection conn, String id) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String userId = null;
		
		String query = prop.getProperty("getUserId");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				userId = rset.getString("m_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return userId;
	}

	public ArrayList<String> selectUser(Connection conn) {
		Statement stmt = null;
		ResultSet rset = null;
		ArrayList<String> userList = new ArrayList<String>();
		
		String query = prop.getProperty("selectUser");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				String user = rset.getString("m_id");
				userList.add(user);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(stmt);
		}
		
		return userList;
	}

	public int createRoom(Connection conn, ChatRoom cr) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("creatRoom");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, cr.getRoomId());
			pstmt.setString(2, cr.getUser1());
			pstmt.setString(3, cr.getUser2());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public ChatRoom selectChatRoom(Connection conn, ChatRoom cr) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ChatRoom room = null;
		
		String query = prop.getProperty("selectChatRoom");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, cr.getUser1());
			pstmt.setString(2, cr.getUser2());
			pstmt.setString(3, cr.getUser1());
			pstmt.setString(4, cr.getUser2());
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				room = new ChatRoom(rset.getString("room_no"), rset.getString("user1"), rset.getString("user2"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return room;
	}

	public ArrayList<Message> selectMessage(Connection conn, ChatRoom room) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Message> msgList = new ArrayList<Message>();
		
		String query = prop.getProperty("selectMessage");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, room.getRoomId());
			pstmt.setString(2, room.getUser1());
			pstmt.setString(3, room.getUser2());
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Message msg = new Message(rset.getInt("msg_no"),
										  rset.getString("room_no"),
										  rset.getString("m_id"),
										  rset.getString("msg_content"));
				msgList.add(msg);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return msgList;
	}

	public int insertMessage(Connection conn, String roomId, String user1, String msg) {
		PreparedStatement pstmt = null;
		int result = 0;
		
		String query = prop.getProperty("insertMessage");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, user1);
			pstmt.setString(2, msg);
			pstmt.setString(3, roomId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public ArrayList<ChatRoom> selectMyChatRoom(Connection conn, String userId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<ChatRoom> roomList = new ArrayList<ChatRoom>();
		
		String query = prop.getProperty("selectMyChatRoom");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.setString(2, userId);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				ChatRoom room = new ChatRoom(rset.getString("room_no"), rset.getString("user1"), rset.getString("user2"));
				roomList.add(room);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return roomList;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
