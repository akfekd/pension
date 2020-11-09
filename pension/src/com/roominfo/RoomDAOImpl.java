package com.roominfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class RoomDAOImpl implements RoomDAO {
	
	private Connection conn=DBConn.getConnection();

	@Override
	public int insertRoominfo(RoomDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="insert into roomInfo (userId, roomId, content, roomName, ment, saveFilename, price, guestnum, created) values"
					+ " (?, ?, ?, ?, ?, ?, ?, ?, sysdate)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getRoomId());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getRoomName());
			pstmt.setString(5, dto.getMent());
			pstmt.setString(6, dto.getSaveFilename());
			pstmt.setString(7, dto.getPrice());
			pstmt.setInt(8, dto.getGuestnum());
			
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
	public int updateRoominfo(RoomDTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="update roomInfo set content=?, roomName=?, ment=?, saveFilename=?, price=?, guestnum=? where roomId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getRoomName());
			pstmt.setString(3, dto.getMent());
			pstmt.setString(4, dto.getSaveFilename());
			pstmt.setString(5, dto.getPrice());
			pstmt.setInt(6, dto.getGuestnum());
			pstmt.setString(7, dto.getRoomId());
			
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
	public String deleteRoominfo(String roomId) throws SQLException {
		String result="";
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="delete from roomInfo where roomId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, roomId);
			
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getString("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				pstmt.close();
			}
			if(rs!=null) {
				rs.close();
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
			sql="SELECT COUNT(*) FROM roominfo";
			pstmt=conn.prepareStatement(sql);
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
			sql="SELECT COUNT(*) FROM roominfo where instr(roomId, ?)>=1";
			
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
	public List<RoomDTO> listRoom(int offset, int rows) {
		List<RoomDTO> list=new ArrayList<RoomDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select userId, roomId, roomName, ment, saveFilename, content, created, price, guestnum "
					+ " from roomInfo order by roomId desc "
					+ " offset ? rows fetch first ? rows only ";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				RoomDTO dto=new RoomDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				dto.setContent(rs.getString("content"));
				dto.setMent(rs.getString("ment"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setCreated(rs.getString("created"));
				dto.setGuestnum(rs.getInt("guestnum"));
				dto.setPrice(rs.getString("price"));
				
				list.add(dto);
			} 
		} catch (SQLException e) {
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
	public List<RoomDTO> listRoom(int offset, int rows, String keyword) {
		List<RoomDTO> list=new ArrayList<RoomDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="select userId, roomId, roomName, ment, saveFilename, content, created, price, guestnum "
					+ " from roomInfo where instr(roomId, ?)>=1 "
					+ " order by roomId desc "
					+ " offset ? rows fetch first ? rows only ";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				RoomDTO dto=new RoomDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				dto.setContent(rs.getString("content"));
				dto.setMent(rs.getString("ment"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setCreated(rs.getString("created"));
				dto.setGuestnum(rs.getInt("guestnum"));
				dto.setPrice(rs.getString("price"));
				
				list.add(dto);
			} 
		} catch (SQLException e) {
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
	public RoomDTO readRoom(String roomId) {
		RoomDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {			
			sql="select userId, roomId, roomName, ment, saveFilename, content, created, price, guestnum "
					+ " from roomInfo where roomId=? ";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, roomId);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new RoomDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setRoomId(rs.getString("roomId"));
				dto.setRoomName(rs.getString("roomName"));
				dto.setMent(rs.getString("ment"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setContent(rs.getString("content"));
				dto.setPrice(rs.getString("price"));
				dto.setGuestnum(rs.getInt("guestnum"));
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
