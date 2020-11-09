package com.manage;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/notice/*")
public class ReservationServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
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
		
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		}else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		}else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խñ� ����Ʈ
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		ReservationDAO dao=new ReservationDAOImpl();
		MyUtil util=new MyUtil();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null) {
			current_page=Integer.parseInt(page);
		}
		
		int dataCount = 0;
		int rows=10;
		dataCount=dao.dataCount(info.getUserId());
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page) 
			current_page=total_page;
		// �ϴ� �����غ�
		int offset=(current_page-1)*rows;
		if(offset<0)
			offset=0;
		
		List<ReservationDTO> list=dao.listBoard(info.getUserId());
		
		// ����Ʈ��ȣ�����
		int listNum, n=0;
		for(ReservationDTO dto:list) {
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String listUrl=cp+"/mypage/list.do";
		String articleUrl=cp+"/mypage/article.do";
		String paging=util.paging(current_page, total_page, listUrl);
		
		// /WEB-INF/views/notice/list.jsp�� �Ѱ��� ������
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
		
		forward(req, resp, "/WEB-INF/views/mypage/list.jsp");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �� ����
		String cp=req.getContextPath();
		ReservationDAO dao=new ReservationDAOImpl();
		
		try {
			int rsvtNum=Integer.parseInt(req.getParameter("rsvtNum"));
			
			// �Խñ�
			ReservationDTO dto=dao.readBoard(rsvtNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/mypage/list.do");
				return;
			}
			
			req.setAttribute("dto", dto);
		
			String path="/WEB-INF/views/mypage/article.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		 
		}
		
		resp.sendRedirect(cp+"/mypage/list.do");
		
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReservationDAO dao=new ReservationDAOImpl();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");

		try {
			int rsvtNum=Integer.parseInt(req.getParameter("rsvtNum"));
			dao.deleteReservation(rsvtNum, info.getUserId());
			
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/mypage/list.do");
	}
	 
}