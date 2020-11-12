package com.reserve;

import java.sql.SQLException;
import java.util.List;

public interface ReserveDAO { //±èµ¿Çö
	public int insertReservation(ReserveDTO dto) throws SQLException;

	public List<RoomInfoDTO> listRoom();
	public List<ReserveDTO> listReserve(String roomId);
	public List<ReserveDTO> listReserve(String roomId, String rsvtStart, String rsvtEnd);
}
