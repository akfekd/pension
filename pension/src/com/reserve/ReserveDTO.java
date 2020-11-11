package com.reserve;

public class ReserveDTO {
	private String userId;
	private String roomId;
	private String rsvtStart;
	private String rsvtEnd;
	private String created;
	private int guestNum;
	private int rsvtNum;
	private String rsvtPrice;
	
	public String getRsvtPrice() {
		return rsvtPrice;
	}
	public void setRsvtPrice(String rsvtPrice) {
		this.rsvtPrice = rsvtPrice;
	}
	public String getRsvtStart() {
		return rsvtStart;
	}
	public void setRsvtStart(String rsvtStart) {
		this.rsvtStart = rsvtStart;
	}
	public String getRsvtEnd() {
		return rsvtEnd;
	}
	public void setRsvtEnd(String rsvtEnd) {
		this.rsvtEnd = rsvtEnd;
	}
	public int getRsvtNum() {
		return rsvtNum;
	}
	public void setRsvtNum(int rsvtNum) {
		this.rsvtNum = rsvtNum;
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
	public String getrsvtStart() {
		return rsvtStart;
	}
	public void setrsvtStart(String rsvtStart) {
		this.rsvtStart = rsvtStart;
	}
	public String getrsvtEnd() {
		return rsvtEnd;
	}
	public void setrsvtEnd(String rsvtEnd) {
		this.rsvtEnd = rsvtEnd;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public int getGuestNum() {
		return guestNum;
	}
	public void setGuestNum(int guestNum) {
		this.guestNum = guestNum;
	}

	
	
}
