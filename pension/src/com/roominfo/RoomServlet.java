package com.roominfo;
//김다현
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;


@MultipartConfig
@WebServlet("/roominfo/*")
public class RoomServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri=req.getRequestURI();
		
		HttpSession session=req.getSession();
		
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+File.separator+"roominfo";
		
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if (uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if (uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if (uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if (uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			RoomDAO dao=new RoomDAOImpl();
			MyUtil util=new MyUtil();
			String cp=req.getContextPath();
			
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
			
			int rows=3;
			int total_page=util.pageCount(rows, dataCount);
			if(current_page>total_page)
				current_page=total_page;
			
			int offset=(current_page-1)*rows;
			
			if(offset<0)
				offset=0;
			
			List<RoomDTO> list;
			if(keyword.length()==0) {
				list=dao.listRoom(offset, rows);
			} else {
				list=dao.listRoom(offset, rows, keyword);
			}
			
			String query="";
			if(keyword.length()!=0) {
				query="keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			String listUrl=cp+"/roominfo/list.do";
			String articleUrl=cp+"/roominfo/article.do?page="+current_page;
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

			forward(req, resp, "/WEB-INF/views/roominfo/list.jsp");
					
		
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/roominfo/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		RoomDAO dao=new RoomDAOImpl();
		try {
			RoomDTO dto=new RoomDTO();
			
			dto.setUserId(info.getUserId());
			dto.setRoomName(req.getParameter("roomName"));
			dto.setContent(req.getParameter("content"));
			dto.setMent(req.getParameter("ment"));
			dto.setRoomId(req.getParameter("roomId"));
			dto.setPrice(req.getParameter("price"));
			dto.setGuestnum(Integer.parseInt(req.getParameter("guestnum")));
			
			String filename=null;
			Part p=req.getPart("selectFile");
			Map<String, String> map=doFileUpload(p, pathname);
			if(map != null) {
				filename=map.get("saveFilename");
				dto.setSaveFilename(filename);
				
				dao.insertRoominfo(dto); // �̹����� �������� ������ DB�� �������� ����
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/roominfo/list.do");
	}
	
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		RoomDAO dao=new RoomDAOImpl();
		String page=req.getParameter("page");
		
		try {
			String roomId=req.getParameter("roomId");
			RoomDTO dto=dao.readRoom(roomId);
			if(dto==null) {
				resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/roominfo/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		RoomDAO dao=new RoomDAOImpl();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page=req.getParameter("page");
		
		try {
			String roomId=req.getParameter("roomId");
			
			RoomDTO dto=dao.readRoom(roomId);
			if(dto==null || ! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			String path="/WEB-INF/views/roominfo/created.jsp";
			forward(req, resp, path);
			return;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
		
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RoomDAO dao=new RoomDAOImpl();
		String cp=req.getContextPath();
		RoomDTO dto=new RoomDTO();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"roominfo/list.do");
			return;
		}
		String page=req.getParameter("page");
		
		try {
			String saveFilename=req.getParameter("saveFilename");
			
			dto.setRoomName(req.getParameter("roomName"));
			dto.setContent(req.getParameter("content"));
			dto.setMent(req.getParameter("ment"));
			dto.setRoomId(req.getParameter("roomId"));
			dto.setPrice(req.getParameter("price"));
			dto.setGuestnum(Integer.parseInt(req.getParameter("guestnum")));
			
			Part p=req.getPart("selectFile");
			Map<String, String> map=doFileUpload(p, pathname);
			if(map!=null) {
				String filename=map.get("saveFilename");
				dto.setSaveFilename(filename);
				
				FileManager.doFiledelete(pathname, saveFilename);
			} else {
				dto.setSaveFilename(req.getParameter("saveFilename"));
			}
			
			dao.updateRoominfo(dto);
			
		} catch (Exception e) {
		}
		
		resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		RoomDAO dao=new RoomDAOImpl();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String page=req.getParameter("page");
		
		try {
			String roomId=req.getParameter("roomId");
			RoomDTO dto=dao.readRoom(roomId);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
				return;
			}
			
			if(! dto.getUserId().equals(info.getUserId()) &&
					! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/roominfo/list.do?page="+page);
				return;
			}
			
			FileManager.doFiledelete(pathname, dto.getSaveFilename());
			
			dao.deleteRoominfo(roomId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/roominfo/list.do/page="+page);
		
	}

}
