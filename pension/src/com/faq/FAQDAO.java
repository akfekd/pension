package com.faq;

import java.sql.SQLException;
import java.util.List;

public interface FAQDAO  {
   public int insertBoard(FAQDTO dto) throws SQLException;
   public int updateBoard(FAQDTO dto) throws SQLException;
   public int deleteBoard(int num, String userId) throws SQLException;
   
   public int dataCount();
   public int dataCount(String condition, String keyword);
   
   public List<FAQDTO> listBoard(int offset, int rows);
   public List<FAQDTO> listBoard(int offset, int rows, String condition, String keyword);
   
   public int updateHitCount(int num) throws SQLException;
   public FAQDTO readBoard(int num);
   public FAQDTO preReadBoard(int num, String condition, String keyword);
   public FAQDTO nextReadBoard(int num, String condition, String keyword);
}