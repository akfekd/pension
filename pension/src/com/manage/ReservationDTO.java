package com.manage;

public class ReservationDTO {
	private int  rsvtNum;
	private String userId;
	private String roomId;
	private String rsvtStart;
	private String rsvtEnd;
	private String created;
	private int guestNum ,listNum;
	private String rsvtPrice;
	private int day;
	
	
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
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
	public int getRsvtNum() {
		return rsvtNum;
	}
	public void setRsvtNum(int rsvtNum) {
		this.rsvtNum = rsvtNum;
	}
	public String getRsvtPrice() {
		return rsvtPrice;
	}
	public void setRsvtPrice(String rsvtPrice) {
		this.rsvtPrice = rsvtPrice;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	
	
}
