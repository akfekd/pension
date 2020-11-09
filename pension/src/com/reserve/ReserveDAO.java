package com.reserve;

import java.sql.SQLException;
import java.util.List;

public interface ReserveDAO {
	public int insertReservation(ReserveDTO dto) throws SQLException;
	public int updateReservation(ReserveDTO dto) throws SQLException;

	public List<RoomInfoDTO> listRoom();
}
