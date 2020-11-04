package com.member;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import com.sun.corba.se.impl.transport.DefaultIORToSocketInfoImpl;

@WebServlet("/member/*")
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd=req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		if(uri.indexOf("login.do")!=-1) {
			loginForm(req, resp);
		} else if (uri.indexOf("login_ok.do")!=-1) {
			loginSubmit(req, resp);
		} else if (uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		} else if (uri.indexOf("member.do")!=-1) {
			memberForm(req, resp);
		} else if (uri.indexOf("member_ok.do")!=-1) {
			memberSubmit(req, resp);
		} else if (uri.indexOf("pwd.do")!=-1) {
			pwdForm(req, resp);
		} else if (uri.indexOf("pwd_ok.do")!=-1) {
			pwdSubmit(req, resp);
		} else if (uri.indexOf("update.do")!=-1) {
			updateFrom(req, resp);
		} else if (uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("userIdCheck.do")!=-1) {
			userIdCheck(req, resp);
		}
		
	}

	protected void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 폼
		String path="/WEB-INF/views/member/login.jsp";
		forward(req, resp, path);
	}

	protected void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 처리
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		try {
			String userId = req.getParameter("userId");
			String userPwd = req.getParameter("userPwd");
			MemberDTO dto = dao.readMemberDTO(userId);
			
			if(dto!=null) {
				if(dto.getUserPwd().equals(userPwd) && dto.getEnabled()==1) {
					// 로그인 성공
					HttpSession session=req.getSession(); // 세션 객체
					
					// 세션 유지시간을 25분으로 설정(톰켓은 기본 30분)
					session.setMaxInactiveInterval(25*60);
					
					// 세션에 저장할 정보
					SessionInfo info=new SessionInfo();
					info.setUserId(dto.getUserId());
					info.setUserName(dto.getUserName());
					
					// 세션에 정보 저장
					session.setAttribute("member", info);
					
					// 메인 화면으로
					resp.sendRedirect(cp);
					return;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 로그인을 실패 한 경우
		req.setAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}

	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃
		HttpSession session=req.getSession();
		
		// 세션에 저장된 모든 정보를 지우기 세션을 초기화
		session.invalidate();
		// 특정한 정보만 삭제할 경우
		// session.removeAttribute("member");
		
		String cp=req.getContextPath();
		resp.sendRedirect(cp);
	}

	protected void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 폼
		req.setAttribute("mode", "member");
		req.setAttribute("title", "회원 가입");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
	}

	protected void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 처리
		// 코딩하기...
		MemberDAO dao=new MemberDAO();
		MemberDTO dto=new MemberDTO();
		String cp=req.getContextPath();
		
		try {
			dto.setUserId(req.getParameter("userId"));
			dto.setUserName(req.getParameter("userName"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setTel(req.getParameter("tel1")+"-"+req.getParameter("tel2")+"-"+req.getParameter("tel3"));
			
			dao.insertMember(dto);
			
			resp.sendRedirect(cp);
			return;
			
		} catch (SQLIntegrityConstraintViolationException e) {
			req.setAttribute("message", "아이디 중복 등의 무결성 제약 조건 위반입니다.");
		} catch (SQLDataException e) {
			req.setAttribute("message", "날짜 형식 등이 잘못 되었습니다.");
		} catch (Exception e) {
			req.setAttribute("message", "데이터 추가가 실패 했습니다.");
		}
		
		req.setAttribute("mode", "member");
		req.setAttribute("title", "회원 가입");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
		
	}

	protected void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정, 탈퇴 등에서 패스워드 입력 폼
	}

	protected void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 검사
	}
	
	protected void updateFrom(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원 정보 수정 폼
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원 정보 수정 처리
	}
	
	protected void userIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원 아이디 중복 검사
	}
	
}
