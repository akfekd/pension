package com.member;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLDataException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.util.MyUtil;

@WebServlet("/member/*")
public class MemberServlet extends HttpServlet{
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
		} else if(uri.indexOf("login_ok.do")!=-1) {
			loginSubmit(req, resp);
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		} else if(uri.indexOf("member.do")!=-1) {
			memberForm(req, resp);
		} else if(uri.indexOf("member_ok.do")!=-1) {
			memberSubmit(req, resp);
		} else if(uri.indexOf("pwd.do")!=-1) {
			pwdForm(req, resp);
		} else if(uri.indexOf("pwd_ok.do")!=-1) {
			pwdSubmit(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("userIdCheck.do")!=-1) {
			userIdCheck(req, resp);
		} else if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}	
	}
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao=new MemberDAO();
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
		if(current_page>total_page) 
			current_page=total_page;
		
		int offset=(current_page-1)*rows;
		if(offset<0)
			offset=0;
		
		List<MemberDTO> list;
		if(keyword.length()==0) {
			list=dao.listMembe(offset, rows);
		} else {
			list=dao.listMember(offset, rows, condition, keyword);
		}
		
		
		// ����Ʈ��ȣ�����
		int listNum, n=0;
		for(MemberDTO dto:list) {
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="
		         +URLEncoder.encode(keyword,"utf-8");
		}
		
		String listUrl=cp+"/member/list.do";
		if(query.length()!=0) {
			listUrl+="?"+query;
		}
		String paging=util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);

		
		String path="/WEB-INF/views/member/list.jsp";
		forward(req, resp, path);
	}
	
	
	protected void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//�α��� ��
		String path="/WEB-INF/views/member/login.jsp";
		forward(req, resp, path);
	}
	
	protected void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//�α��� ó��
		
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		try {
			String userId = req.getParameter("userId");
			String userPwd = req.getParameter("userPwd");
			MemberDTO dto=dao.readMember(userId);
			
			if(dto!=null) {
				if(dto.getUserPwd().equals(userPwd) && dto.getEnabled()==1) {
					//�α��� ����
					HttpSession session=req.getSession(); //���� ��ü
					
					//���ǿ� ������ ����
					SessionInfo info=new SessionInfo();
					info.setUserId(dto.getUserId());
					info.setUserName(dto.getUserName());
					
					//���ǿ� ���� ����
					session.setAttribute("member", info);
					
					//���� ȭ������
					resp.sendRedirect(cp);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//�α����� ���� �Ѱ��
		req.setAttribute("message", "���̵� �Ǵ� �н����尡 �������� �ʽ��ϴ�.");
		forward(req,resp, "/WEB-INF/views/member/login.jsp");
	}
	
	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//�α׾ƿ�
		HttpSession session =req.getSession();
		
		//���ǿ� ����ȸ�� ������ ����� ������ �ʱ�ȭ
		session.invalidate();
		//Ư���� ������ ������ ���
		//session.removeAttribute("member");
		
		String cp=req.getContextPath();
		resp.sendRedirect(cp);
	}
	
	protected void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ȸ��������
		req.setAttribute("mode", "member");
		req.setAttribute("title", "ȸ�� ����");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
	}
	
	protected void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ȸ������ ó��
		
		MemberDTO dto=new MemberDTO();
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		try {
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setTel(req.getParameter("tel1")+"-"+req.getParameter("tel2")+"-"+req.getParameter("tel3"));
			
			dao.insertMember(dto);
			resp.sendRedirect(cp); //http://location:9090/study3
									//http://location:9090/study3/index.jsp
									//http://location:9090/study3/main.do
			return;
			
		} catch (SQLIntegrityConstraintViolationException e) {
			req.setAttribute("message", "���̵� �ߺ� ���� ���Ἲ ���� ���� �����Դϴ�.");
			e.printStackTrace();
		} catch (SQLDataException e) {
			req.setAttribute("message", "��¥ ���ĵ��� �߸� �Ǿ����ϴ�.");
		} catch (Exception e) {
			req.setAttribute("message", "������ �߰��� �����߽��ϴ�.");
		}
		
		req.setAttribute("mode", "member");
		req.setAttribute("title", "ȸ�� ����");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
		
	}
	
	protected void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//���� �������� pwd�Է� ��
	}
	
	protected void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//�н����� �˻�
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ȸ������ ���� ��
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		try {
	
			MemberDTO dto=dao.readMember(info.getUserId());
			if(dto==null || ! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/member/member.do");
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			String path="/WEB-INF/views/member/member.jsp";
			forward(req, resp, path);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/member/member.do");
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ȸ������ ���� ó��
		MemberDTO dto=new MemberDTO();
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		try {
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setTel(req.getParameter("tel1")+"-"+req.getParameter("tel2")+"-"+req.getParameter("tel3"));
			
			dao.updatemember(dto);
			resp.sendRedirect(cp); //http://location:9090/study3
									//http://location:9090/study3/index.jsp
									//http://location:9090/study3/main.do
			return;
			
		} catch (SQLIntegrityConstraintViolationException e) {
			req.setAttribute("message", "���̵� �ߺ� ���� ���Ἲ ���� ���� �����Դϴ�.");
			e.printStackTrace();
		} catch (SQLDataException e) {
			req.setAttribute("message", "��¥ ���ĵ��� �߸� �Ǿ����ϴ�.");
		} catch (Exception e) {
			req.setAttribute("message", "������ �߰��� �����߽��ϴ�.");
		}
		
		req.setAttribute("mode", "update");
		req.setAttribute("title", "ȸ�� ����");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
		
	}
	
	protected void userIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//ȸ�����̵� �ߺ��˻�
	}
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ�� ����
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");

		try {
			if(info.getUserId().equals("admin")) {
			String userId=req.getParameter("userId");
			dao.deleteMember(userId);
			}else {
				String userId=info.getUserId();
				dao.deleteMember(userId);
				session.invalidate();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(info.getUserId().equals("admin")) {
		resp.sendRedirect(cp+"/member/list.do");
		}else {
			resp.sendRedirect(cp);
		}
	}
}
