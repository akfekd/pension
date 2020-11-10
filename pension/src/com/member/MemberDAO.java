package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MemberDAO {
   private Connection conn = DBConn.getConnection();
   
   public int insertMember(MemberDTO dto) throws SQLException {
      int result=0;
      PreparedStatement pstmt = null;
      String sql;
      // member1 테이블과 member2 테이블에 회원 정보 저장
      
      try {
    	  
    	  conn.setAutoCommit(false); //자동 커밋되지 않도록
         sql = "INSERT INTO member1(userId, userName, userPwd, enabled, created, tel) "
               + " VALUES (?,?,?,1,SYSDATE,?) ";
              
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1, dto.getUserId());
         pstmt.setString(2, dto.getUserName());
         pstmt.setString(3, dto.getUserPwd());
           pstmt.setString(4, dto.getTel());

           result=pstmt.executeUpdate();
           
           conn.commit();//커밋
      } catch (SQLIntegrityConstraintViolationException e) {
    	  try {
			conn.rollback();// 예외발생하면 롤백
		} catch (Exception e2) {
		}
    	  e.printStackTrace();
    	  throw e;
      } catch (SQLDataException e) {
    	  try {
  			conn.rollback();// 예외발생하면 롤백
  		} catch (Exception e2) {
  		}
    	  e.printStackTrace();
    	  throw e;
      } catch (SQLException e) {
    	  try {
    			conn.rollback();// 예외발생하면 롤백
    	} catch (Exception e2) {
    	}
         e.printStackTrace();
         throw e;
      } finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
			try {
				conn.setAutoCommit(true);//자동커밋되도록(기본)
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}
   
   //userId 조건에 만족하고
   //member1과 member2를 outer join 하여 MemberDTO 객체에 담아서 반환
   //email은 email,email2에 tel은 tel1, tel2, tel3에 담아저장한다
   public MemberDTO readMember(String userId) {
	   MemberDTO dto=null;
	      PreparedStatement pstmt=null;
	      ResultSet rs=null;
	      StringBuilder sb=new StringBuilder();
	      
	      try {
	         sb.append("SELECT userId, userName, userPwd,");
	         sb.append("      enabled, created,");
	         sb.append("      tel");
	         sb.append("      FROM member1");
	         sb.append("      WHERE userId=?");
	         
	         pstmt=conn.prepareStatement(sb.toString());
	         pstmt.setString(1, userId);
	         rs=pstmt.executeQuery();
	         if(rs.next()) {
	            dto=new MemberDTO();
	            dto.setUserId(rs.getString("userId"));
	            dto.setUserPwd(rs.getString("userPwd"));
	            dto.setUserName(rs.getString("userName"));
	            dto.setEnabled(rs.getInt("enabled"));
	            dto.setTel(rs.getString("Tel"));
	            if(dto.getTel()!=null) {
	               String[] ss=dto.getTel().split("-");
	               if(ss.length==3) {
	                  dto.setTel1(ss[0]);
	                  dto.setTel2(ss[0]);
	                        dto.setTel1(ss[0]);
	               }
	            }
	            
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         if(rs!=null) {
	            try {
	               rs.close();
	            } catch (SQLException e) {
	            }
	         }
	         if(pstmt!=null) {
	            try {
	               pstmt.close();
	            } catch (Exception e) {
	            }
	         }
	      }
	      
	      return dto;

  }
   
   public int updatemember(MemberDTO dto) throws SQLException {
	   int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="UPDATE member1 SET userId=?, userName=?, userPwd=?, tel=? WHERE userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserName());
			pstmt.setString(3, dto.getUserPwd());
			pstmt.setString(4, dto.getTel());
			pstmt.setString(5, dto.getUserId());
			
			result=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		
		return result;
   }
   
   public List<MemberDTO> listMembe() {
	   List<MemberDTO> list=new ArrayList<MemberDTO>();
	   PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT userId, userName, userPwd, ");
			sb.append(" enabled, created,tel ");
			sb.append(" FROM member1");
			sb.append(" ORDER BY userName DESC");
			//sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto=new MemberDTO();
				dto.setUserId(rs.getString("userId")); // dto.setNum(rs.getInt(1));
				dto.setUserName(rs.getString("userName"));  // dto.setName(rs.getString(2));
				dto.setCreated(rs.getString("created"));
				dto.setTel(rs.getString("tel"));
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return list;
   }
   
   public List<MemberDTO> listMember(int offset, int rows, String condition, String keyword) {
	   List<MemberDTO> list=new ArrayList<MemberDTO>();
	   PreparedStatement pstmt=null;
	   ResultSet rs=null;
	   StringBuilder sb=new StringBuilder();
	   
	   try {
			sb.append("SELECT userId, userName, tel, ");
			sb.append(" TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append(" FROM member1");
			
			if(condition.equals("userId")) {
				sb.append("  WHERE INSTR(userId, ?) >= 1 ");
			}else {
				sb.append("  WHERE INSTR(userName, ?) >= 1 ");
			}
			
			sb.append(" ORDER BY userId DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto=new MemberDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setTel(rs.getString("tel"));
				dto.setCreated(rs.getString("created"));
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return list;
	   
   }

   public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) cnt FROM member1";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt("cnt"); // result=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
   }
   
   public int dataCount(String condition, String keyword) {
	// 검색에서의 데이터 개수
			int result=0;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql;
			
			try {
				if(condition.equals("created")) {
					keyword=keyword.replaceAll("(\\-|\\/|\\.)", "");
					sql="SELECT COUNT(*) FROM member1 WHERE TO_CHAR(created, 'YYYYMMDD') = ?";
				} else if(condition.equals("all")) {
					sql="SELECT COUNT(*) FROM member1 WHERE INSTR(userName, ?) >= 1";
				} else {
					sql="SELECT COUNT(*) FROM member1 WHERE INSTR("+condition+", ?) >= 1";
				}
				
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, keyword);
				
				rs=pstmt.executeQuery();
				if(rs.next()) {
					result=rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs!=null) {
					try {
						rs.close();
					} catch (SQLException e2) {
					}
				}
				
				if(pstmt!=null) {
					try {
						pstmt.close();
					} catch (SQLException e2) {
					}
				}
			}
			
			return result;
   }
   
   public int deleteMember(String userId) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM member1 WHERE userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		
		return result;
	}

   
}