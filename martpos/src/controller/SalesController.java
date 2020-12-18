package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Form;
import services.sales.Sales;

@WebServlet({"/JASales","/ASales", "/Sales", "/SearchGoods", "/removeGoods", "/payment"})
public class SalesController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SalesController() {
		super();//부모클래스
	}
	//Front-end도 서버입장에서는 외부파일이기때문에 예외처리를 해주는데, 메소드 예외처리가 throws이다. // try-catch는 메소드 내부의 특정 부분만 예외처리
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//모든 요청값의 기본은 get이다. (post로 지정해야 post로 가지만 post로 보내도 get으로 오는것이 기본)
		Form form = null;
		String mapping = request.getRequestURI().substring(request.getContextPath().length()+1);
		Sales sales;
		if(mapping.equals("Sales")) {
			sales = new Sales(request);
			form = sales.BackController(100);
		}else if(mapping.equals("SearchGoods")) {
			sales = new Sales(request);
			form = sales.BackController(200);
		}else if(mapping.equals("removeGoods"))	{
			sales = new Sales(request);
			form = sales.BackController(300);
		}else if(mapping.equals("payment")) {
			sales = new Sales(request);
			form = sales.BackController(400);
		}else if(mapping.equals("ASales")) {
			sales = new Sales(request);
			form = sales.BackController(500);
		}else if(mapping.equals("JASales")) {
			sales = new Sales(request);
			form = sales.BackController(600);
		}

		// redirect || forward
		if(form.isRedirect()) {
			response.sendRedirect(form.getPage());
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(form.getPage());
			dispatcher.forward(request,response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doGet(request, response);
	}

}
