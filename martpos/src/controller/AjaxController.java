package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Form;
import services.sales.Ajax;
import services.sales.Sales;

@WebServlet({"/ASearchGoods","/JsonAjaxSearchGoods"})
public class AjaxController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AjaxController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mapping = request.getRequestURI().substring(request.getContextPath().length()+1);
		Ajax Aj;
		Sales sales;
		String responseText = null;

		if(mapping.equals("ASearchGoods")) {
			Aj = new Ajax(request);
			responseText = Aj.BackController("500");
		}else if (mapping.equals("JsonAjaxSearchGoods")) {
			Aj = new Ajax(request);
			responseText = Aj.BackController("600");
		}
		System.out.println(responseText);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(responseText);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);


	}

}
