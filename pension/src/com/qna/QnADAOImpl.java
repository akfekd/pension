package com.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class QnADAOImpl implements QnADAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertBoard(QnADTO dto, String mode) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT board_seq.NEXTVAL FROM dual";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			int seq=0;
			if(rs.next()) {
				seq=rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			
			dto.setBoardNum(seq);
			if(mode.equals("created")) {
				// 글을 등록할때
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if(mode.equals("reply")){ 
				// 답글일때
	            // 답글 등록 아래글들의 출력 순서 변경하기
	            updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
	            
	            // 부모글보다 한단계 속으로 들어가게
	            dto.setDepth(dto.getDepth()+1);
	            
	            // 부모글 아래 출력 되도록
	            dto.setOrderNo(dto.getOrderNo()+1);
	         
			}
			
			sql="INSERT INTO qna(boardNum, subject, content, userId, "
				+ " groupNum, orderNo, depth, parent, hitCount, created) "
				+ " VALUES (?,?,?,?,?,?,?,?,0,SYSDATE)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getBoardNum());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getUserId());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getOrderNo());
			pstmt.setInt(7, dto.getDepth());
			pstmt.setInt(8, dto.getParent());
			
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
	public int updateOrderNo(int groupNum, int orderNo) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		//답글일때 답글을 다는 글 아래글들의 출력 순서 변경
		
		try {
			sql="UPDATE qna SET orderNo=orderNo+1 WHERE groupNum=? AND orderNo>?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return result;
	}

	@Override
	public int updateBoard(QnADTO dto) throws SQLException {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE qna SET subject=?, content=? WHERE boardNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3 ,dto.getBoardNum());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int deleteBoard(int boardNum) throws SQLException {
		//삭제
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM qna WHERE boardNum IN "
					+ " ( SELECT boardNum "
					+ "FROM qna START WITH boardNum = ? "
					+ "CONNECT BY PRIOR boardNum=parent ) ";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			result=pstmt.executeUpdate();	
		} catch (Exception e) {
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
	public int updateHitCount(int boardNum) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		// 글보기에서 조회수 증가
		try {
			sql = "UPDATE qna SET hitCount=hitCount+1 WHERE boardNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
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
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT COUNT(*) FROM qna ";
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
	public int dataCount(String condition, String keyword) {
        int result=0;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	sql="SELECT NVL(COUNT(*), 0) FROM qna b JOIN member1 m ON b.userId=m.userId ";
        	if(condition.equals("created")) {
        		keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
        		sql+="  WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
        	} else if(condition.equals("all")) {
        		sql+="  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
        	} else {
        		sql+="  WHERE INSTR(" + condition + ", ?) >= 1 ";
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
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
        
        return result;
	}

	@Override
	public List<QnADTO> listBoard(int offset, int rows) {
		List<QnADTO> list=new ArrayList<QnADTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append(" SELECT boardNum, m.userId, userName, subject, ");
			sb.append("    groupNum, orderNo, depth, hitCount, ");
			sb.append("    TO_CHAR(b.created, 'YYYY-MM-DD') created ");
			sb.append(" FROM qna b ");
			sb.append(" JOIN member1 m ON b.userId = m.userId ");
			sb.append(" ORDER BY groupNum DESC, orderNo ASC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				QnADTO dto=new QnADTO();
				
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setDepth(rs.getInt("depth"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));

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
	public List<QnADTO> listBoard(int offset, int rows, String condition, String keyword) {
        List<QnADTO> list=new ArrayList<QnADTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT boardNum, b.userId, userName,  ");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,  ");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created  ");
			sb.append(" FROM qna b  ");
			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
			if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ");
			} else if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >= 1  ");
			}
			
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
            
			pstmt=conn.prepareStatement(sb.toString());
            if(condition.equals("all")) {
    			pstmt.setString(1, keyword);
    			pstmt.setString(2, keyword);
    			pstmt.setInt(3, offset);
    			pstmt.setInt(4, rows);
            } else {
    			pstmt.setString(1, keyword);
    			pstmt.setInt(2, offset);
    			pstmt.setInt(3, rows);
            }
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
                QnADTO dto=new QnADTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
            
                list.add(dto);
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
				} catch (SQLException e) {
				}
			}
		}
        
        return list;
	}

	@Override
	public QnADTO readBoard(int boardNum) {
		QnADTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT boardNum, b.userId, m.userName, subject, content, hitCount, b.created, "
				+"  groupNum, orderNo, depth, parent"
				+"  FROM qna b JOIN member1 m ON b.userId = m.userId WHERE boardNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto = new QnADTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setDepth(rs.getInt("depth"));
				dto.setParent(rs.getInt("parent"));
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
	public QnADTO preReadBoard(int groupNum, int orderNo, String condition, String keyword) {
        QnADTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT boardNum, subject  ");
    			sb.append(" FROM qna b  ");
    			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND  ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  ");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1 ) AND  ");
            	}
                sb.append("         (( groupNum = ? AND orderNo < ?) OR (groupNum > ? ))  ");
                sb.append(" ORDER BY groupNum ASC, orderNo DESC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setInt(2, groupNum);
                    pstmt.setInt(3, orderNo);
                    pstmt.setInt(4, groupNum);
                }
			} else {
                sb.append("SELECT boardNum, subject FROM qna b JOIN member1 m ON b.userId=m.userId  ");                
                sb.append(" WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ? )  ");
                sb.append(" ORDER BY groupNum ASC, orderNo DESC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QnADTO();
                dto.setBoardNum(rs.getInt("boardNum"));
                dto.setSubject(rs.getString("subject"));
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
				} catch (SQLException e) {
				}
			}
		}
    
        return dto;
	}

	@Override
	public QnADTO nextReadBoard(int groupNum, int orderNo, String condition, String keyword) {
        QnADTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT boardNum, subject  ");
    			sb.append(" FROM qna b  ");
    			sb.append(" JOIN member1 m ON b.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND  ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  ");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1) AND  ");
    			}
                sb.append("          (( groupNum = ? AND orderNo > ?) OR (groupNum < ? ))  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setInt(2, groupNum);
                    pstmt.setInt(3, orderNo);
                    pstmt.setInt(4, groupNum);
                }

			} else {
                sb.append("SELECT boardNum, subject FROM qna b JOIN member1 m ON b.userId=m.userId  ");
                sb.append(" WHERE (groupNum = ? AND orderNo > ?) OR (groupNum < ? )  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QnADTO();
                dto.setBoardNum(rs.getInt("boardNum"));
                dto.setSubject(rs.getString("subject"));
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
				} catch (SQLException e) {
				}
			}
		}

        return dto;
	}

}
