package com.review;
//김다현
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/review/*")
public class ReviewServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
	
		String uri=req.getRequestURI();
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			reviewForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			reviewSubmit(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReviewDAO dao=new ReviewDAOImpl();
		String cp=req.getContextPath();
		MyUtil util=new MyUtil();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null) {
			current_page=Integer.parseInt(page);
		}
		
		String keyword=req.getParameter("keyword");
		if(keyword==null) {
			keyword="";
		}
		int dataCount;
		if(keyword.length()==0) {
			dataCount=dao.dataCount();
		} else {
			dataCount=dao.dataCount(keyword);
		}
		
		int rows=5;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		int offset=(current_page-1)*rows;
		if(offset<0) offset=0;
		
		List<ReviewDTO> list;
		if(keyword.length()==0) {
			list=dao.listReview(offset, rows);
		} else {
			list=dao.listReview(offset, rows, keyword);
		}

		String query="";
		if(keyword.length()!=0) {
			query="keyword="+URLEncoder.encode(keyword,"utf-8");
		}
		
		String listUrl=cp+"/review/list.do";
		String articleUrl=cp+"/review/list.do?page="+current_page;
		if(query.length()!=0) {
			listUrl+="?"+query;
			articleUrl+="&"+query;
		}
		String paging=util.paging(current_page, total_page, listUrl);
				
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);

		forward(req, resp, "/WEB-INF/views/review/list.jsp");
	}
	
	protected void reviewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReviewDAO dao=new ReviewDAOImpl();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");

		List<ReviewDTO> listRsv=dao.listRsv(info.getUserId());
		req.setAttribute("listRsv", listRsv);
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/review/created.jsp");
	}

	
	protected void reviewSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReviewDAO dao=new ReviewDAOImpl();
		ReviewDTO dto=new ReviewDTO();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		
		try {
			dto.setUserId(info.getUserId());
			dto.setUserName(info.getUserName());
			dto.setRsvtNum(Integer.parseInt(req.getParameter("rsvtNum")));
			dto.setContent(req.getParameter("content"));
			dto.setStar(Integer.parseInt(req.getParameter("star")));
			dto.setRoomId(req.getParameter("roomId"));
			dto.setRoomName(req.getParameter("roomName"));
			
			dao.insertReview(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/list.do");
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReviewDAO dao=new ReviewDAOImpl();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page=req.getParameter("page");
		
		try {
			int rsvtNum=Integer.parseInt(req.getParameter("rsvtNum"));
			
			ReviewDTO dto=dao.readReview(rsvtNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/review/list.do?page="+page);
				return;
			}
			
			List<ReviewDTO> listRsv=dao.listRsv(info.getUserId());
			req.setAttribute("listRsv", listRsv);
			req.setAttribute("page", page);
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/review/created.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ReviewDAO dao=new ReviewDAOImpl();
		ReviewDTO dto=new ReviewDTO();
		String cp=req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/review/list.do");
			return;
		}
		String page=req.getParameter("page");
		
		try {
			dto.setRsvtNum(Integer.parseInt(req.getParameter("rsvtNum")));
			dto.setContent(req.getParameter("content"));
			dto.setStar(Integer.parseInt(req.getParameter("star")));
			dto.setRoomId(req.getParameter("roomId"));
			dto.setRoomName(req.getParameter("roomName"));
			
			dao.updateReview(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		ReviewDAO dao=new ReviewDAOImpl();
		
		String page=req.getParameter("page");
		
		try {
			int rsvtNum=Integer.parseInt(req.getParameter("rsvtNum"));
			
			dao.deleteReview(rsvtNum);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/list.do?page="+page);
	}

}
