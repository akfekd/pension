package com.manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReservationDAOImpl implements ReservationDAO{
	private Connection conn=DBConn.getConnection();
	
	@Override
	public List<ReservationDTO> listBoard(String userId) {
		List<ReservationDTO> list=new ArrayList<ReservationDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT rsvtNum, userId, roomId,TO_CHAR(rsvtStart, 'YYYY-MM-DD') rsvtStart, TO_CHAR(rsvtEnd, 'YYYY-MM-DD') rsvtEnd, ");
			sb.append(" TO_CHAR(created, 'YYYY-MM-DD') created, guestNum ");
			sb.append(" FROM reservation");
			sb.append(" WHERE userId=?");
			sb.append(" ORDER BY created DESC");
			//sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto=new ReservationDTO();
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setUserId(rs.getString("userId")); // dto.setNum(rs.getInt(1));
				dto.setRoomId(rs.getString("roomId"));  // dto.setName(rs.getString(2));
				dto.setRsvtStart(rs.getString("rsvtStart"));
				dto.setRsvtEnd(rs.getString("rsvtEnd"));
				dto.setCreated(rs.getString("created"));
				dto.setGuestNum(rs.getInt("guestNum"));
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

	@Override
	public ReservationDTO readBoard(int rsvtNum) {
		ReservationDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT rsvtNum, userId, roomId,TO_CHAR(rsvtStart, 'YYYY-MM-DD') rsvtStart, TO_CHAR(rsvtEnd, 'YYYY-MM-DD') rsvtEnd, "
				+" TO_CHAR(created, 'YYYY-MM-DD') created, guestNum"	
				+"  FROM reservation WHERE rsvtNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, rsvtNum);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto = new ReservationDTO();
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRsvtStart(rs.getString("rsvtStart"));
				dto.setRsvtEnd(rs.getString("rsvtEnd"));
				dto.setCreated(rs.getString("created"));
				dto.setGuestNum(rs.getInt("guestNum"));

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
		
		return dto;
	}

	@Override
	public int dataCount(String userId) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) cnt FROM reservation "
				+" WHERE userId = ? ";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt("cnt");
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

	@Override
	public int deleteReservation(int rsvtNum, String userId) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM reservation WHERE rsvtNum=? AND userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, rsvtNum);
			pstmt.setString(2, userId);
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

	@Override
	public ReservationDTO readBoard(String roomId) {
		ReservationDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT rsvtNum, userId, roomId,TO_CHAR(rsvtStart, 'YYYY-MM-DD') rsvtStart, TO_CHAR(rsvtEnd, 'YYYY-MM-DD') rsvtEnd, "
				+" TO_CHAR(created, 'YYYY-MM-DD') created, guestNum"	
				+"  FROM reservation WHERE roomId=?"
				+" ORDER BY rsvtStart DESC";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, roomId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto = new ReservationDTO();
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRsvtStart(rs.getString("rsvtStart"));
				dto.setRsvtEnd(rs.getString("rsvtEnd"));
				dto.setCreated(rs.getString("created"));
				dto.setGuestNum(rs.getInt("guestNum"));

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
		
		return dto;
	}

	@Override
	public int reservationCount(String roomId) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) cnt FROM reservation "
				+" WHERE roomId = ? ";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, roomId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt("cnt");
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

	@Override
	public List<ReservationDTO> listRaservation(String roomId) {
		List<ReservationDTO> list=new ArrayList<ReservationDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT rsvtNum, userId, roomId,TO_CHAR(rsvtStart, 'YYYY-MM-DD') rsvtStart, TO_CHAR(rsvtEnd, 'YYYY-MM-DD') rsvtEnd, ");
			sb.append(" TO_CHAR(created, 'YYYY-MM-DD') created, guestNum ");
			sb.append(" FROM reservation");
			sb.append(" WHERE roomId=?");
			sb.append(" ORDER BY rsvtStart DESC");
			//sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, roomId);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ReservationDTO dto=new ReservationDTO();
				dto.setRsvtNum(rs.getInt("rsvtNum"));
				dto.setUserId(rs.getString("userId")); // dto.setNum(rs.getInt(1));
				dto.setRoomId(rs.getString("roomId"));  // dto.setName(rs.getString(2));
				dto.setRsvtStart(rs.getString("rsvtStart"));
				dto.setRsvtEnd(rs.getString("rsvtEnd"));
				dto.setCreated(rs.getString("created"));
				dto.setGuestNum(rs.getInt("guestNum"));
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

}
