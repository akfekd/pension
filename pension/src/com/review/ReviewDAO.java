package com.review;

import java.sql.SQLException;
import java.util.List;


public interface ReviewDAO {
	public int insertReview(ReviewDTO dto) throws SQLException;
	public int updateReview(ReviewDTO dto) throws SQLException;
	public int deleteReview(int rsvtNum) throws SQLException;
	

	public int dataCount();
	public int dataCount(String keyword);
	
	public List<ReviewDTO> listReview(int offset, int rows);
	public List<ReviewDTO> listReview(int offset, int rows, String keyword);
	
	public List<ReviewDTO> listRsv(String userId);
}
