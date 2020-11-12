package com.roominfo;
//김다현
public class RoomDTO {
	private String userId;
	private String roomId;
	private String content;
	private String roomName;
	private String created;
	private String saveFilename;
	private String price;
	private int guestnum;
	private String ment;
	
	private String keyword;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getGuestnum() {
		return guestnum;
	}
	public void setGuestnum(int guestnum) {
		this.guestnum = guestnum;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getSaveFilename() {
		return saveFilename;
	}
	public void setSaveFilename(String saveFilename) {
		this.saveFilename = saveFilename;
	}
	public String getMent() {
		return ment;
	}
	public void setMent(String ment) {
		this.ment = ment;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
