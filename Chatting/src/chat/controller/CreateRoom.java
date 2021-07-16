package chat.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import chat.model.service.ChatService;
import chat.model.vo.ChatRoom;
import chat.model.vo.Message;

/**
 * Servlet implementation class CreateRoom
 */
@WebServlet("/createRoom")
public class CreateRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateRoom() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user1 = request.getParameter("user1");
		String user2 = request.getParameter("user2");
		
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int ranNum = (int)(Math.random() * 10000);
		String roomId = sdf.format(new Date(currentTime)) + ranNum;
		
		ChatRoom cr = new ChatRoom(roomId, user1, user2);
		
		ChatRoom room = new ChatService().createRoom(cr);
		ArrayList<ChatRoom> roomList = new ArrayList<ChatRoom>();
		roomList.add(room);
		
		ArrayList<Message> msgList = new ChatService().selectMessage(room);
		
		HashMap<String, ArrayList> content = new HashMap<String, ArrayList>();
		content.put("roomList", roomList);
		content.put("msgList", msgList);
		
		response.setContentType("application/json; charset=utf-8");
		Gson gson = new Gson();		
		gson.toJson(content, response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
