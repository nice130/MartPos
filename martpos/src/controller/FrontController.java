package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import beans.Employee;
import beans.Form;
import services.auth.Approach;

@WebServlet({"/S","/login", "/logout","/moveSales"}) //처음 웹에서 검색했을때 서블릿-컨트롤러로 이동-값이 아무것도 없으면 톰캣의 디폴트서블릿으로 이동->웰컴파일리스트에서 index.html->htm->jsp순서로 이동한다고 예측해 파일이있으면 그곳으로 이동한다.
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String mapping;

	public FrontController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doProcess(request,response);
	}

	private void doProcess(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		HttpSession session = req.getSession();
		mapping = req.getRequestURI().substring(req.getContextPath().length()+1); //컨텍스트패스 : 아이피의 프로잭트명(프로젝트가 여러개일 경우 사용) - 아이피만 설정하면 서버프로젝트로 넘어가게된다.
		Form form = null;
		Approach app;
		switch(mapping) {
		case "S" :
			app = new Approach(req);
			form = app.backController(100);
			break;
		case "login" :
			app = new Approach(req);
			form = app.backController(200);
			break;
		case "logout" :
			app = new Approach(req);
			form = app.backController(300);
			break;
		}
		if(form.isRedirect()) {
			res.sendRedirect(form.getPage());
		}else {
			RequestDispatcher dispatcher = req.getRequestDispatcher(form.getPage());
			dispatcher.forward(req,res);
		}
	}
}
