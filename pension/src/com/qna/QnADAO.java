package com.qna;

import java.sql.SQLException;
import java.util.List;

public interface QnADAO {
	public int insertBoard(QnADTO dto, String mode) throws SQLException;
	public int updateOrderNo(int groupNum, int orderNo) throws SQLException;
	public int updateBoard(QnADTO dto) throws SQLException;
	public int deleteBoard(int boardNum) throws SQLException;
	public int updateHitCount(int boardNum) throws SQLException;
	
	public int dataCount();
	public int dataCount(String condition, String keyword);
	
	public List<QnADTO> listBoard(int offset, int rows);
	public List<QnADTO> listBoard(int offset, int rows, 
			String condition, String keyword);
	
	public QnADTO readBoard(int boardNum);
	public QnADTO preReadBoard(int groupNum, int orderNo, String condition, String keyword);
	public QnADTO nextReadBoard(int groupNum, int orderNo, String condition, String keyword);
}
