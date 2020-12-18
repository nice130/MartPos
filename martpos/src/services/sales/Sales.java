package services.sales;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.Employee;
import beans.Form;
import beans.Order;

public class Sales extends controller.Authentication {
	DataAccessObject dao;
	String page;
	boolean redirect;
	String tag;
	String message;


	public Sales(HttpServletRequest request) {
		//extends하여 부모클래스를 불러오게 되면 묵시적으로 부모클래스의 생성자를 불러오는 super();를 호출한다.
		//하지만 생성자에서 받아온 파라미터가 있기때문에 명시적으로 super를 쓰고 파라미터값인 request를 넘겨주어야한다.
		//Approach를 부모클래스로 사용하지 않고 Sales에서 사용하기 위해서는 Approach app = new Approach(request);를 통해 사용할 클래스를 불러와야한다.

		//현재 extends를 했기 때문에 묵시적으로 super();는 불러와져있는 상태이지만 편의를 위해 생성자에서 초기화를 새로 해주었기 때문에 명시적으로 표시하지 않고, 묵시적이라면 파라미터에 담긴 값이 없기 때문에
		//Approach클래스에서 파라미터에 아무것도 담기지 않은 생성자를 새로 만들어주어야한다.
		//super();
		this.req = request;
		this.form = new Form();
		this.session = this.req.getSession();
		this.page = "index.jsp";
		this.redirect = true;


		//로딩 : 생성자를 힙에 올리는 것 / 오버로딩 : 생성자를 두번 호출하는 것 | 파라미터의 개수나 타입이 다르면 생성자를 두번 호출(오버로딩)하는것이 가능하다.
	}

	public Form BackController(int serviceCode) {

		if(session.getAttribute("sCode") != null) {
			switch(serviceCode) {
			case 100 :
				moveSales();
				break;
			case 200 :
				searchGoods();
				break;
			case 300 :
				removeGoods();
				break;
			case 400 :
				payment();
				break;
			case 500 :
				ajaxPosmain();
				break;
			case 600 :
				JASales();
				break;
			}
			
		} else {
			form.setPage(page);
			form.setRedirect(redirect);
		}
		return form;
	}
	private void JASales() {
		page = "jsonposmain.jsp";
		redirect = false;
		Employee emp;
		emp = getEmpInfo();

		req.setAttribute("empName", emp.getEmName());
		req.setAttribute("accessTime", emp.getLastAccessTime());
		
		form.setPage(page);
		form.setRedirect(redirect);
	}
	
	private void ajaxPosmain() {
		page = "ajaxsPosmain.jsp";
		redirect = false;
		
		Employee emp;
		emp = getEmpInfo();

		req.setAttribute("empName", emp.getEmName());
		req.setAttribute("accessTime", emp.getLastAccessTime());

		form.setPage(page);
		form.setRedirect(redirect);
	}

	private void payment() {
		page = "posmain.jsp";
		redirect = false;
		ArrayList<Order> list = null;
		boolean result = false;

		Order order = new Order();
		//고객코드 확인 - 비회원 / 회원
		order.setOrderCode(this.req.getParameter("orderCode"));
		order.setEmpCode(order.getOrderCode().substring(0, 3));
		order.setOrderDate(order.getOrderCode().substring(order.getEmpCode().length(), order.getEmpCode().length()+14));
		order.setCuCode(order.getOrderCode().substring(order.getEmpCode().length()+order.getOrderDate().length()));
		order.setOrderState("C");

		dao = new DataAccessObject();
		dao.setTransaction(false);

		if(this.req.getParameterValues("cuCode")[0].length()==11) {

			order.setCuCode(this.req.getParameterValues("cuCode")[0]);

			result = insOrders(order); //emp, date, cu, C
			dao.setTransactionResult(result);

			order.setCuCode("9999       ");

			list = selOrderDetail(order);

			order.setCuCode(this.req.getParameterValues("cuCode")[0]);
			for(Order od : list) {
				od.setCuCode(order.getCuCode());
				result = updOrderDetail(od);//emp, date, cu, goods, quantity
				if(!result) {break;}
			}
			dao.setTransactionResult(result);

			result = delOrders(order);
			dao.setTransactionResult(result);

			order.setCuCode(this.req.getParameterValues("cuCode")[0]);

			list = selOrderDetail(order);
			for(Order upd : list) {
				result = updGoods(upd);
				if(!result) {break;}
			}
			dao.setTransactionResult(result);

		} else if (this.req.getParameterValues("cuCode")[0].equals("9999")) {

			result = updOrderState(order);
			dao.setTransactionResult(result);

			list = selOrderDetail(order);
			for(Order upd : list) {
				result = updGoods(upd);
				if(!result) {break;}
			}
			dao.setTransactionResult(result);
		} 
		
		message = sumTotalAmount(list)+"원이 결제 되었습니다.";
		this.req.setAttribute("message", message);


		dao.setTransaction(true);
		dao.dbClose();

		form.setPage(page);
		form.setRedirect(redirect);
	}


	private boolean updOrderDetail(Order order) {
		return dao.updOrderDetail(order);
	}

	private boolean updOrderState(Order order) {
		return dao.updOrderState(order);
	}

	private boolean updGoods(Order order) {
		return dao.updGoods(order);
	}


	private void searchGoods() {
		page = "index.jsp";
		redirect = true;
		ArrayList<Order> list = null;
		tag = null;
		message = null;

		if(session.getAttribute("sCode")!=null) {
			page = "posmain.jsp";
			redirect = false;

			Order order = new Order();

			order.setEmpCode(session.getAttribute("sCode").toString());
			order.setCuCode("9999       ");
			order.setGoodsCode(this.req.getParameter("goodsCode"));
			// sales에 표현되는 부분인데 날짜는 데타베에서 불러올거니까 위의입력해야하는 정보만 order에 넣는다?

			dao = new DataAccessObject();

			boolean isGoodsCode = isGoodsCode(order);

			dao.setTransaction(!isGoodsCode);

			if(!isOrderCode()) {

				if(isGoodsCode) {

					order.setQuantity(1);
					order.setOrderState("P");

					if(insOrders(order)) {
						selOrderDate(order);

						order.setOrderCode(order.getEmpCode()+order.getOrderDate()+order.getCuCode());

						dao.setTransactionResult(insOrderDetail(order));

						list = selOrderDetail(order);

						tag = makeHtmlGoodsList(list);

						Employee emp = getEmpInfo();

						this.req.setAttribute("goodsList", tag);
						this.req.setAttribute("empName", emp.getEmName());
						this.req.setAttribute("accessTime", emp.getLastAccessTime());
						this.req.setAttribute("orderCode", order.getOrderCode());
						this.req.setAttribute("totalAmount", sumTotalAmount(list));
					} else { message = "주문상품이 입력되지 않았습니다. 다시 입력해 주세요~";}
				}else { message = "판매상품이 아닙니다.";}
			} else {

				order.setOrderCode(this.req.getParameter("orderCode"));
				order.setEmpCode(order.getOrderCode().substring(0, 3));
				order.setOrderDate(order.getOrderCode().substring(order.getEmpCode().length(), order.getEmpCode().length()+14));
				order.setCuCode(order.getOrderCode().substring(order.getEmpCode().length()+order.getOrderDate().length()));

				if(isGoodsCode(order)) {

					order.setQuantity(1);

					boolean isInsOrderDetail = insOrderDetail(order);
					dao.setTransactionResult(isInsOrderDetail);
					if(!isInsOrderDetail)message = "주문상품이 입력되지 않았습니다. 다시 입력해 주세요~";
				} else {message = "판매상품이 아닙니다.";}

				list = selOrderDetail(order);

				tag = makeHtmlGoodsList(list);

				Employee emp = getEmpInfo();

				this.req.setAttribute("goodsList", tag);
				this.req.setAttribute("empName", emp.getEmName());
				this.req.setAttribute("accessTime", emp.getLastAccessTime());
				this.req.setAttribute("orderCode", order.getOrderCode());
				this.req.setAttribute("totalAmount", sumTotalAmount(list));
			}
			this.req.setAttribute("message", message);
			dao.setTransaction(!isGoodsCode);
			dao.dbClose();
		}

		form.setPage(page);
		form.setRedirect(redirect);
	}



	private void removeGoods() {
		page = "posmain.jsp";
		redirect = false;
		Order order = new Order();
		ArrayList<Order> list = new ArrayList<Order>();
		tag = null;

		order.setOrderCode(this.req.getParameter("orderCode"));
		order.setEmpCode(order.getOrderCode().substring(0, 3));
		order.setOrderDate(order.getOrderCode().substring(order.getEmpCode().length(), order.getEmpCode().length()+14));
		order.setCuCode(order.getOrderCode().substring(order.getEmpCode().length()+order.getOrderDate().length()));
		order.setGoodsCode(this.req.getParameter("goCode"));

		dao = new DataAccessObject();
		dao.setTransaction(false);

		boolean delOrderDetail = delOrderDetail(order);


		list = selOrderDetail(order);
		if(list.size()==0) {
			delOrderDetail = delOrders(order);
			order.setOrderCode("");
		}
		dao.setTransactionResult(delOrderDetail);
		tag = makeHtmlGoodsList(list);

		Employee emp = getEmpInfo();

		this.req.setAttribute("goodsList", tag);
		this.req.setAttribute("empName", emp.getEmName());
		this.req.setAttribute("accessTime", emp.getLastAccessTime());
		this.req.setAttribute("orderCode", order.getOrderCode());
		this.req.setAttribute("totalAmount", sumTotalAmount(list));


		dao.setTransaction(true);
		dao.dbClose();

		form.setPage(page);
		form.setRedirect(redirect);

	}
	private boolean delOrders(Order order) {
		return dao.delOrders(order);
	}

	private boolean delOrderDetail(Order order) {
		return dao.delOrderDetail(order);
	}


	private boolean isGoodsCode(Order order) {
		return dao.isGoodsCode(order);
	}
	//1. orderCode유무
	private boolean isOrderCode() {
		return (this.req.getParameter("orderCode") != "")? true:false;
	}
	//주문테이블 Ins : orderCode생성
	private boolean insOrders(Order order) {
		//dao 호출
		return dao.insOrders(order);
	}
	//주문테이블 Select : orderCode조회
	private void selOrderDate(Order order) {
		dao.selOrderDate(order);
	}
	//주문상세테이블 Ins : 주문코드, 상품코드
	private boolean insOrderDetail(Order order) {
		return dao.insOrderDetail(order);
	}
	//주문상품조회
	
	private ArrayList<Order> selOrderDetail(Order order) {
		return dao.selOrderDetail(order);
	}
	
	//makeHtml
	private String makeHtmlGoodsList(ArrayList<Order> list) {
		StringBuffer tag = new StringBuffer();

		for(Order order : list) {
			tag.append("<tr onDblClick=\"callRemove(\'"+order.getGoodsCode()+"\')\">");

			tag.append("<td>"+order.getGoodsCode()+"</td>");
			tag.append("<td>"+order.getGoodsName()+"</td>");
			tag.append("<td>"+order.getGoodsPrice()+"</td>");
			tag.append("<td>"+order.getQuantity()+"</td>");
			tag.append("<td>"+order.getAmount()+"</td>");

			tag.append("</tr>");
		}
		return tag.toString();
	}
	

	//  \가 붙어있는 "나'는 html에서 사용할 문자처리된 "'이고 \가 안붙은 것은 자바에서 사용하는 "'이다.
	 //<input type=\"int\">
	private int sumTotalAmount(ArrayList<Order> list) {
		int totalAmount = 0;
		for(Order orderBean : list) {
			totalAmount += orderBean.getAmount();
		}
		return totalAmount;
	}
	
	private void moveSales() {

		if(session.getAttribute("sCode")!=null) {
			Employee emp;
			emp = getEmpInfo();

			req.setAttribute("empName", emp.getEmName());
			req.setAttribute("accessTime", emp.getLastAccessTime());

			page = "posmain.jsp";
			redirect = false;
		}
	
		form.setPage(page);
		form.setRedirect(redirect);
	}
	
	/*String page = "posmain.jsp";
	boolean redirect = false;
	//세션 체크 : 로그인이 되어있는지 아닌지 파악
	if(session.getAttribute("sCode")!=null) {
		//bean생성
		Employee emp = new Employee();
		//empCode 저장
		emp.setEmCode(session.getAttribute("sCode").toString());
		//DAO 연결
		dao = new DataAccessObject(); //services.sales의 DataAccessObject로 가서 controller의 DataAccessObject로 가서 getEmpInfo를 가져온다.
		//사용정보값 가져오기
		dao.getEmpInfo(emp);
		//request.setAttribute
		req.setAttribute("empName", emp.getEmName());
		req.setAttribute("accessTime",emp.getLastAccessTime());

	} */

	private Employee getEmpInfo() {
		Employee emp = new Employee();
		emp.setEmCode(session.getAttribute("sCode").toString());

		dao = new DataAccessObject();

		dao.getEmpInfo(emp);

		return emp;
	}
}
