package com.review;
//김다현
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReviewDAOImpl implements ReviewDAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertReview(ReviewDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="insert into review (userId, userName, rsvtNum, content, created, star) values "
					+ " (?, ?, ?, ?, sysdate, ?)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserName());
			pstmt.setInt(3, dto.getRsvtNum());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getStar());
			
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
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
	public int updateReview(ReviewDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="update review set content=?, star=? where rsvtNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getContent());
			pstmt.setInt(2, dto.getStar());
			pstmt.setInt(3, dto.getRsvtNum());
			
			result=pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				pstmt.close();
			}
		}
		
		return result;
	}

	@Override
	public int deleteReview(int rsvtNum) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="delete from review where rsvtNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, rsvtNum);
			
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

	@Override
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select count(*) from review v"
					+ " join member1 m on v.userId=m.userId";
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
	public int dataCount(String keyword) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select count(*) from review v"
					+ " join member1 m on v.userId=m.userId join reservation r on r.rsvtNum=v.rsvtNum"
					+ " where instr(roomId, ?)>=1";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			
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
	public List<ReviewDTO> listReview(int offset, int rows) {
		List<ReviewDTO> list=new ArrayList<ReviewDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("select userName, v.userId, r.roomId, f.roomname, v.rsvtNum, v.content, v.created, star"); 
			sb.append(" from review v join reservation r on r.rsvtNum=v.rsvtNum join roomInfo f on r.roomId=f.roomId");
			sb.append(" order by created desc");
			sb.append(" offset ? rows fetch first ? rows only");
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto=new ReviewDTO();
				String userName=rs.getString("userName");
				userName=userName.replace(userName.charAt(1), '*');
				dto.setUserName(userName);
				
				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				dto.setContent(rs.getNString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setStar(rs.getInt("star"));
				
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
	public List<ReviewDTO> listReview(int offset, int rows, String keyword) {
		List<ReviewDTO> list=new ArrayList<ReviewDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("select userName, v.userId, r.roomId, f.roomname, v.rsvtNum, v.content, v.created, star"); 
			sb.append(" from review v join reservation r on r.rsvtNum=v.rsvtNum join roomInfo f on r.roomId=f.roomId");
			sb.append(" where instr(r.roomId, ?)>=1");
			sb.append(" order by created desc");
			sb.append(" offset ? rows fetch first ? rows only");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto=new ReviewDTO();				
				String userName=rs.getString("userName");
				userName=userName.replace(userName.charAt(1), '*');
				dto.setUserName(userName);

				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				dto.setContent(rs.getNString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setStar(rs.getInt("star"));
				
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
	public List<ReviewDTO> listRsv(String userId) {
		List<ReviewDTO> list=new ArrayList<ReviewDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select r.userId, r.rsvtNum, r.roomId, f.roomName from reservation r "
					+ " join roominfo f on r.roomId=f.roomId where r.userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto=new ReviewDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				
				list.add(dto);
			}
			
		}  catch (Exception e) {
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
	public ReviewDTO readReview(int rsvtNum) {
		ReviewDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select rsvtNum, userId, content, created, star, userName from review where rsvtNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, rsvtNum);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new ReviewDTO();
				
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setStar(rs.getInt("star"));
				dto.setUserName(rs.getString("userName"));
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
