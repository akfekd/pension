package com.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReserveDAOImpl implements ReserveDAO { // ±èµ¿Çö
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertReservation(ReserveDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			
			sql="INSERT INTO reservation (userId, roomId, rsvtStart, rsvtEnd, created, guestnum, rsvtNum, rsvtPrice)"
					+ "VALUES (?, ?, ?, ?, SYSDATE, ?, RESERVATION_SEQ.NEXTVAL, ?)";
		
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getRoomId());
			pstmt.setString(3, dto.getRsvtStart());
			pstmt.setString(4, dto.getRsvtEnd());
			pstmt.setInt(5, dto.getGuestNum());
			pstmt.setString(6, dto.getRsvtPrice());

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
	public List<RoomInfoDTO> listRoom() {
		List<RoomInfoDTO> list=new ArrayList<RoomInfoDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
	
		try {
			
			String sql;
			sql="SELECT roomId, roomName, guestnum, price, saveFilename FROM roominfo ";
		
			pstmt=conn.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				RoomInfoDTO dto=new RoomInfoDTO();
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				dto.setGuestnum(rs.getInt("guestnum"));
				dto.setPrice(rs.getString("price"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				list.add(dto);
			}
		} catch (Exception e) {
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
	public List<ReserveDTO> listReserve(String roomId) {
		List<ReserveDTO> listReserve=new ArrayList<ReserveDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
	
		try {
			
			String sql;
			sql="SELECT roomId, guestnum, rsvtPrice,rsvtStart,rsvtEnd FROM reservation ";
		
			pstmt=conn.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ReserveDTO dto=new ReserveDTO();
				dto.setRoomId(rs.getString("roomId"));
				dto.setGuestNum(rs.getInt("guestnum"));
				dto.setRsvtPrice(rs.getString("rsvtPrice"));
				dto.setRsvtStart(rs.getString("rsvtStart"));
				dto.setRsvtEnd(rs.getString("rsvtEnd"));
				listReserve.add(dto);
			}
		} catch (Exception e) {
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
		return listReserve;
	}



	@Override
	public List<ReserveDTO> listReserve(String roomId, String rsvtStart, String rsvtEnd) {
		List<ReserveDTO> listReserve=new ArrayList<ReserveDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
	
		try {
			
			String sql;
			sql="SELECT roomId, guestnum, rsvtPrice,rsvtStart,rsvtEnd FROM reservation "
				+ " WHERE roomId=? "
    			+" AND ( ( rsvtStart >= TO_DATE(?) AND rsvtEnd < TO_DATE(?) ) "
	    		+"   OR ( rsvtStart <= TO_DATE(?) AND rsvtEnd >= TO_DATE(?) ) "
		    	+"   OR ( rsvtEnd > TO_DATE(?) AND rsvtEnd < TO_DATE(?) ) "
		    	+"   OR ( rsvtStart >= TO_DATE(?) AND rsvtStart < TO_DATE(?) ) ) ";

			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, roomId);
			
			pstmt.setString(2, rsvtStart);
			pstmt.setString(3, rsvtEnd);
			
			pstmt.setString(4, rsvtStart);
			pstmt.setString(5, rsvtEnd);
			
			pstmt.setString(6, rsvtStart);
			pstmt.setString(7, rsvtEnd);
			
			pstmt.setString(8, rsvtStart);
			pstmt.setString(9, rsvtEnd);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ReserveDTO dto=new ReserveDTO();
				dto.setRoomId(rs.getString("roomId"));
				dto.setGuestNum(rs.getInt("guestnum"));
				dto.setRsvtPrice(rs.getString("rsvtPrice"));
				dto.setRsvtStart(rs.getString("rsvtStart"));
				dto.setRsvtEnd(rs.getString("rsvtEnd"));
				listReserve.add(dto);
			}
		} catch (Exception e) {
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
		return listReserve;

	}
}
