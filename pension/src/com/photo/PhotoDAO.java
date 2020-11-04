package com.photo;

import java.sql.SQLException;
import java.util.List;

public interface PhotoDAO {
	public int insertPhoto(PhotoDTO dto) throws SQLException;
	public int updatePhoto(PhotoDTO dto) throws SQLException;
	public int deletePhoto(int num) throws SQLException;
	
	public int dataCount();
	public List<PhotoDTO> listPhoto(int offset, int rows);
	public PhotoDTO readPhoto(int num);
}
