package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertMember(MemberDTO dto) throws SQLException{
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		// member1 테이블과 member2 테이블에 회원 정보 저장

		// 코딩하기...
		
		try {
		
			conn.setAutoCommit(false); // 자동 커밋 되지 않도록
			
			sql="INSERT INTO member1(userId,userName,userPwd,enabled,created,tel)" + "VALUES (?,?,?,1,SYSDATE,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserName());
			pstmt.setString(3, dto.getUserPwd());
			pstmt.setString(4, dto.getTel());

			result=pstmt.executeUpdate();	
			
			conn.commit(); // 커밋
			
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
				conn.rollback(); // 예외가 발생하면 롤백
			} catch (Exception e2) {				// TODO: handle exception
			}
			e.printStackTrace();
			throw e;
		}
		catch (SQLDataException e) {
			try {
				conn.rollback(); // 예외가 발생하면 롤백
			} catch (Exception e2) {				// TODO: handle exception
			}
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			try {
				conn.rollback(); // 예외가 발생하면 롤백
			} catch (Exception e2) {				// TODO: handle exception
			}
			e.printStackTrace();
			throw e;
		}
		finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
			
			try {
				conn.setAutoCommit(true); // 자동 커밋 되도록
			} catch (Exception e2) {
			}
			
		}
		
	
		return result;
	}

/*
	userId 조건에 만족하는
	member1과 member2를 outer join 하여 MemberDTO 객체에 담아서 반환
	email은 email1, email2에 tel은 tel1, tel2, tel3에  담아 저장한다.
	email이나 tel이 null인 경우에는 아무것도 담지 않는다. 
*/
	
	public MemberDTO readMemberDTO(String userId) {
		MemberDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append("SELECT userId, userName, userPwd, " );
			sb.append("		enabled, created, tel " );
			sb.append("		FROM member1 WHERE userId=? " );
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setEnabled(rs.getInt("enabled"));
				dto.setCreated(rs.getString("created"));
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
	
}


