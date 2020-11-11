package com.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.festival.BoardDAO;
import com.festival.BoardDAOImpl;
import com.festival.BoardDTO;
import com.restaurant.RestaurantDAO;
import com.restaurant.RestaurantDAOImpl;
import com.restaurant.RestaurantDTO;
import com.spot.SpotDAO;
import com.spot.SpotDAOImpl;
import com.spot.SpotDTO;
import com.util.MyServlet;

@WebServlet("/main.do")
public class MainServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		BoardDAO dao =new BoardDAOImpl();
		List<BoardDTO> list2;
		list2 = dao.listCount();
		
		SpotDAO dao1 =new SpotDAOImpl();
		List<SpotDTO> list3;
		list3 = dao1.listCount();
		
		RestaurantDAO dao2 =new RestaurantDAOImpl();
		List<RestaurantDTO> list4;
		list4 = dao2.listCount();
	
		
		req.setAttribute("list2", list2);
		req.setAttribute("list3", list3);
		req.setAttribute("list4", list4);
		
		
		if(uri.indexOf("main.do")!=-1) {
			forward(req, resp, "/WEB-INF/views/main/main.jsp");
		}
	}
}
