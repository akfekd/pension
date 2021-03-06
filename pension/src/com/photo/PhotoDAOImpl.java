package com.photo;
////김성원
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class PhotoDAOImpl implements PhotoDAO{
   private Connection conn=DBConn.getConnection();

   @Override
   public int insertPhoto(PhotoDTO dto) throws SQLException {
      int result=0;
      PreparedStatement pstmt=null;
      String sql;
      
      try {
         sql="INSERT INTO photo1(num,subject, content, userId, saveFilename, created) "
               + "VALUES(photo_seq.NEXTVAL,?, ?, ?, ?, SYSDATE) ";
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1, dto.getSubject());
         pstmt.setString(2, dto.getContent());
         pstmt.setString(3, dto.getUserId());
         pstmt.setString(4, dto.getImageFilename());
         
         result=pstmt.executeUpdate();

      } catch (Exception e) {
         e.printStackTrace();
         throw e;
      } finally {
         if(pstmt!=null) {
            try {
               pstmt.close();
            } catch (Exception e2) {
            }
         }
      }
      return result;
   }

   @Override
   public int updatePhoto(PhotoDTO dto) throws SQLException {
      int result=0;
      PreparedStatement pstmt=null;
      String sql;
      
      try {
         sql="UPDATE photo1 SET subject=?, content=?, saveFilename=? WHERE num=?";
      
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1, dto.getSubject());
         pstmt.setString(2, dto.getContent());
         pstmt.setString(3, dto.getImageFilename());
         pstmt.setInt(4, dto.getNum());
         result=pstmt.executeUpdate();
   } catch (SQLException e) {
      e.printStackTrace();
      throw e;
   }finally {
      if(pstmt!=null) {
         try {
            pstmt.close();
         } catch (Exception e2) {
            
         }
      }
   }
      return result;
   }

   @Override
   public int deletePhoto(int num) throws SQLException {
      int result=0;
      PreparedStatement pstmt=null;
      String sql;
      
      try {
         sql="DELETE FROM photo1 WHERE num=?";
      
         pstmt=conn.prepareStatement(sql);

         pstmt.setInt(1, num);
         result=pstmt.executeUpdate();
      
   } catch (SQLException e) {
      e.printStackTrace();
      throw e;
   }finally {
      if(pstmt!=null) {
         try {
            pstmt.close();
         } catch (Exception e2) {
            
         }
      }
   }
      return result;
   }

   @Override
   public int dataCount() { //데이터갯수
      int result=0;
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      String sql;
      
      try {
         sql="SELECT COUNT(*) FROM photo1";
         pstmt=conn.prepareStatement(sql);
         rs=pstmt.executeQuery();
         if(rs.next()) {
            result=rs.getInt(1);
         }
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if(rs!=null) {
            try {
               rs.close();
            } catch (Exception e2) {
            }
         }
         if(pstmt!=null) {
            try {
               pstmt.close();
            } catch (Exception e2) {
            }
         }
      }
      
      return result;
   }

   @Override
   public List<PhotoDTO> listPhoto(int offset, int rows) {
      List<PhotoDTO> list=new ArrayList<PhotoDTO>();
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      String sql;
      
      try {
         //이름 가져와야해서 조인해야함
         sql="SELECT num, p.userId, userName, subject, saveFilename"
               + "  FROM photo1 p"
               + "  JOIN member1 m ON p.userId=m.userId"
               + "  ORDER BY num DESC"
               + "  OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
         
         pstmt=conn.prepareStatement(sql);
         pstmt.setInt(1, offset);
         pstmt.setInt(2, rows);
         rs=pstmt.executeQuery();
         
         while(rs.next()) {
            PhotoDTO dto = new PhotoDTO();
            dto.setNum(rs.getInt("num"));
            dto.setUserId(rs.getString("userId"));
            dto.setUserName(rs.getString("userName"));
            dto.setSubject(rs.getString("subject"));
            dto.setImageFilename(rs.getString("saveFilename"));
            
            list.add(dto);
         }
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if(rs!=null) {
            try {
               rs.close();
            } catch (Exception e2) {
            }
         }
         if(pstmt!=null) {
            try {
               pstmt.close();
            } catch (Exception e2) {
            }
         }
      }
      
      return list;
   }

   @Override
   public PhotoDTO readPhoto(int num) {
      PhotoDTO dto =null;
      PreparedStatement pstmt=null;
      ResultSet rs =null;
      String sql;
      
      try {
      sql= "SELECT num, p.userId, userName, subject, content ,saveFilename,created "
                  + "  FROM photo p"
                  + "  JOIN member1 m ON p.userId=m.userId"
                  + "  WHERE num = ?" ;
      pstmt= conn.prepareStatement(sql);
      pstmt.setInt(1, num);
      rs=pstmt.executeQuery();
      
      if(rs.next()) {
         dto = new PhotoDTO(); 
            dto.setNum(rs.getInt("num"));
            dto.setUserId(rs.getString("userId"));
            dto.setUserName(rs.getString("userName"));
            dto.setSubject(rs.getString("subject"));
            dto.setContent(rs.getString("content"));
            dto.setImageFilename(rs.getString("saveFilename"));
            dto.setCreated(rs.getString("created"));
      }
      
   } catch (Exception e) {
     e.printStackTrace();
   } finally {
         if(rs!=null) {
            try {
               rs.close();
            } catch (Exception e2) {
            }
         }
         if(pstmt!=null) {
            try {
               pstmt.close();
            } catch (Exception e2) {
            }
         }
      }
      
      return dto;
   }


}