<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ArrayList, chat.model.vo.ChatRoom" %>
<%
	String userId = (String)request.getAttribute("userId");
	ArrayList<String> userList = (ArrayList<String>)request.getAttribute("userList");
	ArrayList<ChatRoom> roomList = (ArrayList<ChatRoom>)request.getAttribute("roomList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.1/css/all.css" integrity="sha384-vp86vTRFVJgpjF9jiIGPEEqYqlDwgyBgEF109VFjmqGmIY/Y4HV4d3Gp2irVfcrp" crossorigin="anonymous">
	<link rel="stylesheet" href="css/chat.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
</head>
<body>
	<% if(userId == null) { %>
		<input type="text" id="id">
		<button id="login">login</button>
	<% } else { %>
		<span><%= userId %></span>
	<% } %>
	
	<div id="userList">
		<% if(userList != null) { %>
			<% for(int i = 0; i < userList.size(); i++) { %>
				<% String user = userList.get(i); %>
				<% if(!user.equals(userId)) { %>
					<div class="user">
						<span style="margin-right: 15px;"><%= user %></span>
						<% for(int j = 0; j < roomList.size(); j++) { %>
							<% ChatRoom room = roomList.get(j); %>
							<% if(user.equals(room.getUser1()) || user.equals(room.getUser2())) { %>
<%-- 							<% break;} %> --%>
<%-- 							<% if(j == roomList.size() - 1) { %> --%>
<!-- 								<i class="far fa-comment"></i> -->
								<input type="hidden" value="<%= room.getRoomId() %>">
							<% break;} %>
						<% } %>
	<%-- 					<input type="hidden" value="<%= userList.get(i) %>"> --%>
						<i class="fas fa-comment"></i>
					</div>
				<% } %>
			<% } %>
		<% } %>
	</div>
	
<!-- 	<div class="chatRoom"> -->
<!-- 		<div class="chatContent"> -->
<!-- 			<div class="chatLeft"> -->
<!-- 				<div>user01</div> -->
<!-- 				<div class="chat_msg_box"><span class="chatLeft_msg">안녕하세요</span></div> -->
<!-- 			</div> -->
<!-- 			<div class="chatRight"> -->
<!-- 				<span class="chatRight_msg">안녕하세요</span> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<input type="text" class="msgWrite"> -->
<!-- 		<button class="submitBtn">전송</button> -->
<!-- 	</div> -->

		<div class="aa">
		</div>
	
	<script>
		$('#login').on('click', function(){
			var id = $('#id').val();
			location.href="<%= request.getContextPath() %>/login?id=" + id;
		});
		
		$('.fas').on('click', function(){
			var user1 = "<%= userId %>";
			var user2 = $(this).parent().children().eq(0).text();
// 			var roomNo = $(this).parent().children().eq(1).val();
			$('.aa').html("");
			
			$.ajax({
				url: 'createRoom',
				data: {user1:user1, user2:user2},
				success: function(data) {
					var roomList = data["roomList"];
					var msgList = data["msgList"];
// 					console.log(msgList);
// 					console.log(roomList);
// 					console.log(typeof(msgList[0].mId));
// 					console.log(typeof(user1));
					
					addHtml = "";
					addHtml += "<div class='chatRoom'>";
					addHtml += "<input type='hidden' value='" + roomList[0].roomId + "'>";
					addHtml += "<input type='hidden' value='" + roomList[0].user2 + "'>";
					addHtml += "<div class='chatContent'>";
					for(var i = 0; i < msgList.length; i++) {
						var msg = msgList[i];
						
						if(msg.mId != "<%= userId %>") {
							addHtml += "<div class='chatLeft'>";
							addHtml += "<div>" + msg.mId + "</div>";
							addHtml += "<div class='chat_msg_box'><span class='chatLeft_msg'>" + msg.msgContent + "</span></div>";
							addHtml += "</div>";
						} else {
							addHtml += "<div class='chatRight'>";
							addHtml += "<span class='chatRight_msg'>" + msg.msgContent + "</span>";
							addHtml += "</div>";
						}
					}
					addHtml += "</div>";
					addHtml += "<input type='text' class='msgWrite'>";
					addHtml += "<button class='submitBtn'>전송</button>";
					addHtml += "</div>";
					
					$('.aa').append(addHtml);
					$('.chatContent').scrollTop($('.chatContent')[0].scrollHeight);
					connectSocket(roomList[0].roomId);
				} 
			});			
		});
		
		$(document).on('click', '.submitBtn', function(){
			var roomId = $(this).parent().children().eq(0).val();
			var user1 = "<%= userId %>";
			var user2 = $(this).parent().children().eq(1).val();
			var msg = $(this).parent().children().eq(3).val();
			$(this).parent().children().eq(3).val("");
			
			sendMsg(roomId, user1, user2, msg);
		});
		
		var webSocket = null;
		
		function connectSocket(roomId) {
			webSocket = new WebSocket("ws://localhost:9180/Chatting/websocket");
			
			var area = $('.chatContent');
			
			webSocket.onopen = function(message) {
// 				area.append("connect...\n");
			}
			
			webSocket.onmessage = function(message) {
				var msg = message.data.split("/");
				
				if(msg[0] == roomId) {
					var addHtml = "";
					addHtml += "<div class='chatLeft'>";
					addHtml += "<div>" + msg[1] + "</div>";
					addHtml += "<div class='chat_msg_box'><span class='chatLeft_msg'>" + msg[2] + "</span></div>";
					addHtml += "</div>";
					
					area.append(addHtml);		
					area.scrollTop(area[0].scrollHeight);
				}
					
			}
		}
		
		function sendMsg(roomId, user1, user2, msg) {
			var addHtml = "";
			addHtml += "<div class='chatRight'>";
			addHtml += "<span class='chatRight_msg'>" + msg + "</span>";
			addHtml += "</div>";
			
			$('.chatContent').append(addHtml);
			$('.chatContent').scrollTop($('.chatContent')[0].scrollHeight);
			webSocket.send(roomId + "/" + user1 + "/" + user2 + "/" + msg);
		}
	</script>
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
</body>
</html>