package chat.model.vo;

public class Message {
	private int msgNo;
	private String roomId;
	private String mId;
	private String msgContent;
	
	public Message() {}

	public Message(int msgNo, String roomId, String mId, String msgContent) {
		super();
		this.msgNo = msgNo;
		this.roomId = roomId;
		this.mId = mId;
		this.msgContent = msgContent;
	}

	public int getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(int msgNo) {
		this.msgNo = msgNo;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	@Override
	public String toString() {
		return "Message [msgNo=" + msgNo + ", roomId=" + roomId + ", mId=" + mId + ", msgContent=" + msgContent + "]";
	}
	
}
