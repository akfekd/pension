package com.reserve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bbs.BoardDTO;
import com.member.MemberDTO;
import com.util.DBConn;

public class ReserveDAOImpl implements ReserveDAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertReservation(ReserveDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			
			sql="INSERT INTO reservation (userId, roomId, rsvtStart, rsvtEnd, created, guestnum, rsvtNum)"
					+ "VALUES (?, ?, ?, ?, SYSDATE, ?, RESERVATION_SEQ.NEXTVAL)";
		
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getRoomId());
			pstmt.setString(3, dto.getrsvtStart());
			pstmt.setString(4, dto.getrsvtEnd());
			pstmt.setInt(5, dto.getGuestNum());

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
	public int updateReservation(ReserveDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			// ¸¶Àú ¼ÕºÁ¾ß µÊ roominfo¿¡¼­ Á¤º¸ ¹Þ¾Æ¿Ã °Å °í¹Î
			sql="UPDATE reservation SET rsvStart=?, rsvEnd=?, guestnum=?, WHERE roomId=?, AND userId=?";
			
		} catch (Exception e) {
			e.printStackTrace();
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

}
