package com.qna;

import java.io.IOException;
import java.net.URLDecoder;
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

@WebServlet("/qna/*")
public class QnAServlet extends MyServlet {
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
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("reply.do")!=-1) {
			replyForm(req, resp);
		} else if(uri.indexOf("reply_ok.do")!=-1) {
			replySubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트
		QnADAO dao=new QnADAOImpl();
		MyUtil util=new MyUtil();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null) {
			current_page=Integer.parseInt(page);
		}
		
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		if(condition==null) {
			condition="all";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword, "utf-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount=dao.dataCount();
		} else {
			dataCount=dao.dataCount(condition, keyword);
		}
		
		int rows=10;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page) {
			current_page=total_page;
		}
		
		int offset=(current_page-1)*rows;
		if(offset<0) {
			offset=0;
		}
		
		List<QnADTO> list;
		if(keyword.length()==0) {
			list=dao.listBoard(offset, rows);
		} else {
			list=dao.listBoard(offset, rows, condition, keyword);
		}
		
		int listNum, n=0;
		for(QnADTO dto : list){
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="
		         +URLEncoder.encode(keyword,"utf-8");
		}
		
		String listUrl=cp+"/qna/list.do";
		String articleUrl=cp+"/qna/article.do?page="+current_page;
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
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/qna/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기폼
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/qna/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글저장
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		
		QnADAO dao=new QnADAOImpl();
		try {
			QnADTO dto=new QnADTO();
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dao.insertBoard(dto, "created");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qna/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		QnADAO dao=new QnADAOImpl();
		String cp=req.getContextPath();
		
		try {
			int boardNum=Integer.parseInt(req.getParameter("boardNum"));
			String page=req.getParameter("page");
			
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword=URLDecoder.decode(keyword,"utf-8");
			
			String query="page="+page;
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+
			         URLEncoder.encode(keyword,"utf-8");
			}
			
			// 조회수
			dao.updateHitCount(boardNum);
			
			// 게시글
			QnADTO dto=dao.readBoard(boardNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			// 이전/다음 글
			QnADTO preReadDto=dao.preReadBoard(dto.getGroupNum(),
					dto.getOrderNo(), condition, keyword);
			QnADTO nextReadDto=dao.nextReadBoard(dto.getGroupNum(),
					dto.getOrderNo(), condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			String path="/WEB-INF/views/qna/article.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do");
		
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정폼
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정완료
	}
	
	protected void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변폼
		QnADAO dao =new QnADAOImpl();
		String cp = req.getContextPath();
		String page= req.getParameter("page");
		
		try {
			int boardNum=Integer.parseInt(req.getParameter("boardNum"));
			QnADTO dto = dao.readBoard(boardNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/qna/list.do?page="+page);
				return;
			}
			String s="["+dto.getSubject()+"]에 대한 답변입니다.\n";
			dto.setContent(s);
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "reply");
			
			forward(req, resp, "/WEB-INF/views/qna/created.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변저장
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		
		String page= req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		QnADAO dao = new QnADAOImpl();
		
		try {
			QnADTO dto = new QnADTO();
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
			dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
			dto.setDepth(Integer.parseInt(req.getParameter("depth")));
			dto.setParent(Integer.parseInt(req.getParameter("parent")));
			
			dao.insertBoard(dto, "reply");
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();

		String page= req.getParameter("page");
		String query="page="+page;
		
		QnADAO dao = new QnADAOImpl();
		
		try {
			String condition=req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword=URLDecoder.decode(keyword,"utf-8");
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			QnADTO dto = dao.readBoard(boardNum);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			if(!dto.getUserId().equals(info.getUserId())&&!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			
			
			dao.deleteBoard(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
}
