package com.festival;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class BoardDAOImpl implements BoardDAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertBoard(BoardDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="INSERT INTO festival ( num,userId, content,subject, created, hitCount,imageFilename) "
					+ "  VALUES (festival_seq.NEXTVAL,?, ?, ?, SYSDATE,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getSubject());
			pstmt.setInt(4, dto.getHitCount());
			pstmt.setString(5, dto.getImageFilename());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLIntegrityConstraintViolationException e) {	
			throw e;
		} catch (SQLDataException e) {	
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}	finally {
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
	public int updateBoard(BoardDTO dto) throws SQLException {
		  int result=0;
	      PreparedStatement pstmt=null;
	      String sql;
	      
	      try {
	         sql="UPDATE festival SET subject=?, content=?, imageFilename=? WHERE num=?";
	      
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
	public int deleteBoard(int num, String userId) throws SQLException {
		  int result=0;
	      PreparedStatement pstmt=null;
	      String sql;
	      
	      try {
	         sql="DELETE FROM festival WHERE num=?";
	      
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
	public int dataCount() {
		 int result = 0;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      String sql;

	      try {
	         sql = " SELECT COUNT(*) FROM festival f "
	         +" JOIN member1 m ON f.userId = m.userId ";
	         pstmt = conn.prepareStatement(sql);
	         rs = pstmt.executeQuery();
	         if (rs.next()) {
	            result = rs.getInt(1); 
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         if (rs != null) {
	            try {
	               rs.close();
	            } catch (Exception e2) {
	            }
	         }
	         if (pstmt != null) {
	            try {
	               pstmt.close();
	            } catch (Exception e2) {
	            }
	         }
	      }
	      return result;
	}

	@Override
	public int dataCount(String condition, String keyword) {
		 int result=0;
	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        String sql;

	        try {
	            sql="SELECT COUNT(*) FROM festival f "
	                     + " JOIN member1 m ON f.userId = m.userId ";

	               if(condition.equals("f.created")) {
	                  keyword=keyword.replaceAll("(\\-|\\/|\\.)", ""); 
	                  sql+=" WHERE TO_CHAR(f.created, 'YYYYMMDD')=?";
	               }else if(condition.equals("all")) {
	                  sql+="  WHERE INSTR(subject, ?) >=1 OR INSTR(content,?) >= 1";
	               }else {
	                  sql+=" WHERE INSTR("+condition+", ?)>=1";
	               }

	           
	            pstmt=conn.prepareStatement(sql);
	            pstmt.setString(1, keyword);
	            
	            if(condition.equals("all")) {
	               pstmt.setString(2, keyword);
	            }

	            rs=pstmt.executeQuery();

	            if(rs.next())
	                result=rs.getInt(1);
	            
	            
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         if(rs!=null) {
	            try   {
	               rs.close();
	            }catch (SQLException e2){
	            }
	         }
	         if(pstmt!=null) {
	            try   {
	               pstmt.close();
	            }catch (SQLException e2){
	            }
	         }
	      }

	        return result;
	   }

	@Override
	public List<BoardDTO> listBoard(int offset, int rows) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb= new StringBuilder();
		
		try {
			sb.append("   SELECT num, userName, subject, ");
			sb.append("   content, f.created, hitCount, imageFilename");
			sb.append("   FROM festival f ");
			sb.append("   JOIN member1 m ");
			sb.append("   ON f.userId = m.userId ");
			sb.append("   ORDER BY num DESC ");
			sb.append("   OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
	        pstmt.setInt(1, offset);
	        pstmt.setInt(2, rows);
			
			
	        rs = pstmt.executeQuery();
	         while (rs.next()) {
	            BoardDTO dto = new BoardDTO();
	            dto.setNum(rs.getInt("num"));
	            dto.setUserName(rs.getString("userName"));
	            dto.setSubject(rs.getString("subject"));
	            dto.setContent(rs.getString("content"));
	            dto.setCreated(rs.getString("created"));
	            dto.setHitCount(rs.getInt("hitCount"));
	            dto.setImageFilename(rs.getNString("imageFilename"));
	            
	            list.add(dto);
	         }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
	         if (rs != null) {
	             try {
	                rs.close();
	             } catch (Exception e2) {
	             }
	          }
	          if (pstmt != null) {
	             try {
	                pstmt.close();
	             } catch (Exception e2) {
	             }
	          }
	       }
	       return list;
	}

	@Override
	public List<BoardDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb= new StringBuilder();
		
		try {
			sb.append(" SELECT num, userName, subject, content, ");
			sb.append(" hitCount, f.created, imageFilename ");
			sb.append(" FROM festival f ");
			sb.append("  JOIN member1 m ");
			sb.append(" ON f.userId = m.userId ");
			
			if(condition.equals("f.created")) {
				keyword=keyword.replaceAll("(\\-|\\/|\\.)","");
				sb.append(" WHERE TO_CHAR(f.created,'YYYY-MM-DD') = ?");
			}else if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject,?) >= 1 OR INSTR(content, ?) >= 1 ");
			}else {
				sb.append(" WHERE INSTR("+condition+", ?) >= 1");
			}
			
			sb.append("   ORDER BY num DESC ");
			sb.append("   OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			if(condition.equals("all")) {
	            pstmt.setString(1, keyword);
	            pstmt.setString(2, keyword);
	            pstmt.setInt(3, offset);
	            pstmt.setInt(4, rows);
	         }else {  
		        pstmt.setString(1, keyword);
		        pstmt.setInt(2, offset);
		        pstmt.setInt(3, rows);
	         }

	        rs = pstmt.executeQuery();
	        
	         while (rs.next()) {
	            BoardDTO dto = new BoardDTO();
	            dto.setNum(rs.getInt("num"));
	            dto.setUserName(rs.getString("userName"));
	            dto.setSubject(rs.getString("subject"));
	            dto.setContent(rs.getString("content"));
	            dto.setHitCount(rs.getInt("hitCount"));
	            dto.setCreated(rs.getString("created"));
	            dto.setImageFilename(rs.getNString("imageFilename"));
	            
	            list.add(dto);
	         }  
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
	         if (rs != null) {
	             try {
	                rs.close();
	             } catch (Exception e2) {
	             }
	          }
	          if (pstmt != null) {
	             try {
	                pstmt.close();
	             } catch (Exception e2) {
	             }
	          }
	       }
		return list;
	}

	@Override
	public BoardDTO readBoard(int num) {
		  BoardDTO dto = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      String sql;

	      try {
	         sql = "SELECT num, m.userId, userName, subject, content, hitCount, f.created, imageFilename FROM festival f JOIN member1 m "
	         		+ " ON f.userId = m.userId WHERE num=?";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, num);
	         rs = pstmt.executeQuery();
	         if (rs.next()) {
	            dto = new BoardDTO();
	            dto.setNum(rs.getInt("num"));
	            dto.setUserId(rs.getString("userId"));
	            dto.setUserName(rs.getString("userName"));
	            dto.setSubject(rs.getString("subject"));
	            dto.setContent(rs.getString("content"));
	            dto.setHitCount(rs.getInt("hitCount"));
	            dto.setCreated(rs.getString("created"));
	            dto.setImageFilename(rs.getNString("imageFilename"));
	            
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         if (rs != null) {
	            try {
	               rs.close();
	            } catch (Exception e2) {
	            }
	         }
	         if (pstmt != null) {
	            try {
	               pstmt.close();
	            } catch (Exception e2) {
	            }
	         }
	      }
	      return dto;	
	}

	@Override
	public BoardDTO preBoard(int num, String condition, String keyword) {
		 // 이전글
        BoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject, content FROM festival f  ");
                sb.append("  JOIN member1 m ");
    			sb.append(" ON f.userId = m.userId ");
    			if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >=1 OR INSTR(content,?) >= 1) ");
    			} else if(condition.equals("userName")) {
                    sb.append(" WHERE ( INSTR(userName, ?) = 1)  ");
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
                } else{
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("            AND (num > ? ) ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
             	   pstmt.setString(1, keyword);
             	   pstmt.setString(2, keyword);
                   	pstmt.setInt(3, num);
                }else {
                 pstmt.setString(1, keyword);
                	pstmt.setInt(2, num);
                }
            } else {
                sb.append(" SELECT num, subject FROM festival ");
                sb.append(" WHERE num > ? ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new BoardDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
              
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (Exception e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (Exception e2){
				}
			}
		}
    
        return dto;
	}

	@Override
	public BoardDTO nextBoard(int num, String condition, String keyword) {
		 // 다음글
        BoardDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT num, subject,content FROM festival f  ");
                sb.append("  JOIN member1 m ");
    			sb.append(" ON f.userId = m.userId ");
    			if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >=1 OR INSTR(content,?) >= 1) ");
    			} else if(condition.equals("userName")) {
                    sb.append(" WHERE ( INSTR(userName, ?) = 1)  ");
                } else if(condition.equals("created")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ?) ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("            AND (num < ? ) ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
               if(condition.equals("all")) {
            	   pstmt.setString(1, keyword);
            	   pstmt.setString(2, keyword);
                  	pstmt.setInt(3, num);
               }else {
                pstmt.setString(1, keyword);
               	pstmt.setInt(2, num);
               }
            } else {
                sb.append(" SELECT num, subject FROM festival ");
                sb.append(" WHERE num < ? ");
                sb.append(" ORDER BY num DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new BoardDTO();
                dto.setNum(rs.getInt("num"));
                dto.setSubject(rs.getString("subject"));
                
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try	{
					rs.close();
				}catch (Exception e2){
				}
			}
			if(pstmt!=null) {
				try	{
					pstmt.close();
				}catch (Exception e2){
				}
			}
		}
    
        return dto;
	}

	@Override
	public int updateHitcount(int num) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE festival SET hitCount = hitCount+1 WHERE num = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}

	

