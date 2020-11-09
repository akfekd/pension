package com.festival;


import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.util.MyUtil;
import com.util.MyUploadServlet;

@MultipartConfig
@WebServlet("/festival/*")	//이거 안넣으면 404 나옴
public class BoardServlet  extends MyUploadServlet {
	private static final long serialVersionUID = 1L;

	private String pathname;
	@Override
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info==null) {	//로그인이 되어 있지 않으면
			String cp = req.getContextPath();
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		 //이미지 저장할 경로
	      //이미지는 반드시 루트 아래에 폴더를 만들고 올려야함
	      String root=session.getServletContext().getRealPath("/");
	      pathname=root+"uploads"+File.separator+"festival";
	      
	      
		String uri = req.getRequestURI();
		if(uri.indexOf("list.do")!=-1) {
			list(req,resp);
		}else if(uri.indexOf("created.do")!=-1) {
			createdForm(req,resp);
		}else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req,resp);
		}else if(uri.indexOf("article.do")!=-1) {
			article(req,resp);
		}else if(uri.indexOf("update.do")!=-1) {
			updateForm(req,resp);
		}else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req,resp);
		}else if(uri.indexOf("delete.do")!=-1) {
			delete(req,resp);
		}
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAOImpl();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition==null) {
			condition="all";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword,"UTF-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount=dao.dataCount();
		}else {
			dataCount = dao.dataCount(condition,keyword);	
		}
		
		int rows=10;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page = total_page;
		
		int offset=(current_page-1)*rows;
		if(offset<0) {
			offset=0;
		}
		
		List<BoardDTO> list;
		if(keyword.length()==0) {
			list = dao.listBoard(offset,rows);
		}else {
			list = dao.listBoard(offset, rows,condition,keyword);
		}
		
		//리스트 번호 만들기
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		int listNum, n =0;
		long gap;
		
		for(BoardDTO dto : list) {
			listNum = dataCount-(offset+n);
			dto.setListNum(listNum);
			
			try {
				Date date = sdf.parse(dto.getCreated());
				gap=(curDate.getTime() - date.getTime()) / (1000*60*60);
				dto.setGap(gap);
			} catch (Exception e) {
			}
			
			dto.setCreated(dto.getCreated().substring(0,10));
			
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
		}
		
		String listUrl = cp+"/festival/list.do";
		String articleUrl = cp+"/festival/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl+="?"+query;
			articleUrl +="&"+query;
		}
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
	
		String path = "/WEB-INF/views/festival/list.jsp";
		forward(req, resp, path);
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글쓰기 폼
		req.setAttribute("mode", "created");
		String path = "/WEB-INF/views/festival/created.jsp";
		forward(req, resp, path);
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//글
		BoardDAO dao = new BoardDAOImpl();
		BoardDTO dto = new BoardDTO();
		String cp = req.getContextPath();
		
		try {
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			dto.setUserId(info.getUserId());//글을 등록하는 사람
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			 String filename=null;
			 Part p=req.getPart("selectFile");
	         Map<String, String> map=doFileUpload(p, pathname);
		     if(map!=null) {
			     filename=map.get("saveFilename");
			     dto.setImageFilename(filename);
		     }
		     dao.insertBoard(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/festival/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAOImpl();
		String cp =req.getContextPath();
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			String page =  req.getParameter("page");
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition="all";
				keyword = "";
			}
			
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			String query ="page="+page;
			if(keyword.length()!=0) {
				query +="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			//조회수 
			dao.updateHitcount(num);
			
			//게시글
			BoardDTO dto = dao.readBoard(num);
			if(dto == null) {
				resp.sendRedirect(cp+"/festival/list.do?"+query);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			BoardDTO preDto = dao.preBoard(num, condition, keyword);
			BoardDTO nextDto = dao.nextBoard(num, condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preDto", preDto);
			req.setAttribute("nextDto", nextDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			String path = "/WEB-INF/views/festival/article.jsp";
			forward(req,resp,path);
			return;
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAOImpl();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info =(SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			BoardDTO dto =dao.readBoard(num);
			
			if(dto == null || ! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/festival/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			String path = "/WEB-INF/views/festival/created.jsp";
			forward(req,resp,path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/festival/list.do?page="+page);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAOImpl();
		String cp = req.getContextPath();
		String page= req.getParameter("page");
		
		HttpSession session = req.getSession();
		SessionInfo info =(SessionInfo)session.getAttribute("member");
		
		
		try {
			BoardDTO dto = new BoardDTO();
			 String imageFilename=req.getParameter("imageFilename");
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setUserId(info.getUserId());//글을 수정하는 사람
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Part p =req.getPart("selectFile");
			Map<String, String> map =doFileUpload(p, pathname);
			if(map!=null) {
			//새로운 이미지를 등록한 경우
			String filename=map.get("saveFilename");
			dto.setImageFilename(filename);			         
			//기존파일 지우기
			FileManager.doFiledelete(pathname,imageFilename);
			}else {
			//이미지 파일이 변경 하지 않는 경우
			dto.setImageFilename(imageFilename);
			}
			
			dao.updateBoard(dto);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/festival/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDAO dao = new BoardDAOImpl();
		String cp = req.getContextPath();
		String page= req.getParameter("page");
		String query="page="+page;

		HttpSession session = req.getSession();
		SessionInfo info =(SessionInfo)session.getAttribute("member");
		
		try {
			int num = Integer.parseInt(req.getParameter("num")); 
			String userId= info.getUserId();//글을 수정하는 사람
				
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			//파일지우기
			BoardDTO dto = dao.readBoard(num);
			FileManager.doFiledelete(pathname,dto.getImageFilename());
			dao.deleteBoard(num,userId);
			
			if(condition==null) {
				condition="subject";
				keyword = "";
			}
			keyword=URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/festival/list.do?"+query);
	}
}
