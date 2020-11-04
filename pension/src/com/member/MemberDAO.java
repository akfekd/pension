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
		// member1 ���̺�� member2 ���̺� ȸ�� ���� ����

		// �ڵ��ϱ�...
		
		try {
		
			conn.setAutoCommit(false); // �ڵ� Ŀ�� ���� �ʵ���
			
			sql="INSERT INTO member1(userId,userName,userPwd,enabled,created,tel)" + "VALUES (?,?,?,1,SYSDATE,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserName());
			pstmt.setString(3, dto.getUserPwd());
			pstmt.setString(4, dto.getTel());

			result=pstmt.executeUpdate();	
			
			conn.commit(); // Ŀ��
			
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
				conn.rollback(); // ���ܰ� �߻��ϸ� �ѹ�
			} catch (Exception e2) {				// TODO: handle exception
			}
			e.printStackTrace();
			throw e;
		}
		catch (SQLDataException e) {
			try {
				conn.rollback(); // ���ܰ� �߻��ϸ� �ѹ�
			} catch (Exception e2) {				// TODO: handle exception
			}
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			try {
				conn.rollback(); // ���ܰ� �߻��ϸ� �ѹ�
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
				conn.setAutoCommit(true); // �ڵ� Ŀ�� �ǵ���
			} catch (Exception e2) {
			}
			
		}
		
	
		return result;
	}

/*
	userId ���ǿ� �����ϴ�
	member1�� member2�� outer join �Ͽ� MemberDTO ��ü�� ��Ƽ� ��ȯ
	email�� email1, email2�� tel�� tel1, tel2, tel3��  ��� �����Ѵ�.
	email�̳� tel�� null�� ��쿡�� �ƹ��͵� ���� �ʴ´�. 
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


