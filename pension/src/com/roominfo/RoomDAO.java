package com.roominfo;

import java.sql.SQLException;
import java.util.List;

public interface RoomDAO {
	public int insertRoominfo(RoomDTO dto) throws SQLException;
	public int updateRoominfo(RoomDTO dto) throws SQLException;
	public String deleteRoominfo(String roomId) throws SQLException;
	
	public int dataCount();
	public int dataCount(String keyword);
	
	public List<RoomDTO> listRoom(int offset, int rows);
	public List<RoomDTO> listRoom(int offset, int rows, String keyword);
	
	public RoomDTO readRoom(String roomId);
	
}
