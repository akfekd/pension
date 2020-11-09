package com.reserve;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;

@WebServlet("/reserve/*")
public class ReserveServlet extends HttpServlet {
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
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		if(uri.indexOf("reserve.do")!=-1) {
			reserve(req, resp);
		} else if(uri.indexOf("reserve_ok.do")!=-1) {
			reserveSubmit(req, resp);
		}
		
		
	}
	
	protected void reserve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 예약 화면 리스트
		ReserveDAO dao=new ReserveDAOImpl();
		
		// 룸정보
		List<RoomInfoDTO> listRoom;
		listRoom=dao.listRoom();
		req.setAttribute("listRoom", listRoom);
		
		String path="/WEB-INF/views/reserve/reserve.jsp";
		req.setAttribute("mode", "reserve");
		forward(req, resp, path);
	}
	
	protected void reserveForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 예약 폼
		String path="/WEB-INF/views/reserve/reserve.jsp";
		req.setAttribute("mode", "reserve");
		forward(req, resp, path);
	}
	
	protected void reserveSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReserveDAO dao=new ReserveDAOImpl();
		ReserveDTO dto=new ReserveDTO();
		String cp=req.getContextPath();
		
		try {
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			dto.setUserId(info.getUserId());
			dto.setRoomId(req.getParameter("roomId"));
			dto.setGuestNum(Integer.parseInt(req.getParameter("guestNum")));
			dto.setPrice(req.getParameter("price"));
			dto.setrsvtStart(req.getParameter("rsvtStart"));
			dto.setrsvtEnd(req.getParameter("rsvtEnd"));
			
			dao.insertReservation(dto);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/mypage/list.do");
	}
	
	protected void b(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

}
