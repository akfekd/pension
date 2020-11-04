package com.bbs;

import java.sql.SQLException;
import java.util.List;

public interface BoardDAO  {
	public int insertBoard(BoardDTO dto) throws SQLException;
	public int updateBoard(BoardDTO dto) throws SQLException;
	public int deleteBoard(int num, String userId) throws SQLException;
	
	public int dataCount();
	public int dataCount(String condition, String keyword);
	
	public List<BoardDTO> listBoard(int offset, int rows);
	public List<BoardDTO> listBoard(int offset, int rows, String condition, String keyword);
	
	public int updateHitCount(int num) throws SQLException;
	public BoardDTO readBoard(int num);
	public BoardDTO preReadBoard(int num, String condition, String keyword);
	public BoardDTO nextReadBoard(int num, String condition, String keyword);
}
