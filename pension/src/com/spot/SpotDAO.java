package com.spot;

import java.sql.SQLException;
import java.util.List;



public interface SpotDAO {
	public int insertSpot(SpotDTO dto) throws SQLException;
	public int updateSpot(SpotDTO dto) throws SQLException;
	public int deleteSpot(int num, String userId) throws SQLException;
	
	public int dataCount();
	public int dataCount(String condition, String keyword);
	
	public List<SpotDTO> listCount();
	public List<SpotDTO> listSpot(int offset, int rows);
	public List<SpotDTO> listSpot(int offset, int rows, String condition, String keyword);

	public int updateHitcount(int num);
	public SpotDTO readSpot(int num);
	public SpotDTO preSpot(int num, String condition, String keyword);
	public SpotDTO nextSpot(int num, String condition, String keyword);
}
