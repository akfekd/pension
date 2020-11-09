package com.manage;

import java.sql.SQLException;
import java.util.List;

public interface ReservationDAO {
	public List<ReservationDTO> listBoard(String userId);
	public ReservationDTO readBoard(int rsvtNum);
	public int deleteReservation(int rsvtNum, String userId) throws SQLException;
	public int dataCount(String userId);
}