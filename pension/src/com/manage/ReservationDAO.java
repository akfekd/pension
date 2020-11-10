package com.manage;

import java.sql.SQLException;
import java.util.List;

public interface ReservationDAO {
	public List<ReservationDTO> listBoard(String userId);
	public List<ReservationDTO> listRaservation(String roomId);
	public ReservationDTO readBoard(int rsvtNum);
	public ReservationDTO readBoard(String roomId);
	public int deleteReservation(int rsvtNum, String userId) throws SQLException;
	public int dataCount(String userId);
	public int reservationCount(String roomId);
	
} 
