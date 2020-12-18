package services.sales;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import beans.Order;

public class Ajax extends controller.Authentication {
	DataAccessObject dao;
	String page;
	boolean redirect;
	String tag;
	String message;


	public Ajax(HttpServletRequest request) {
		this.req = request;
		this.session = this.req.getSession();
		this.page = "index.jsp";
		this.redirect = true;
	}

	public String BackController (String ajaxCode) {
		String responseText = "일단 서비스 접속";
		if(session.getAttribute("sCode") != null) {
			switch(ajaxCode) {
			case "500" :
				responseText = ASearchGoods();
				break;
			case "600" :
				responseText=JsonAjaxSearchGoods();
				break;
			}
		} else {
			responseText = "로그인하세요";
		}
		return responseText;
	}

	private String JsonAjaxSearchGoods() {
		ArrayList<Order> list = null;
		tag = null;

		Order order = new Order();

		order.setEmpCode(session.getAttribute("sCode").toString());
		order.setCuCode("9999       ");
		order.setGoodsCode(this.req.getParameter("goodsCode"));
		// sales에 표현되는 부분인데 날짜는 데타베에서 불러올거니까 위의입력해야하는 정보만 order에 넣는다?

		dao = new DataAccessObject();
		boolean isGoodsCode = isGoodsCode(order);
		dao.setTransaction(!isGoodsCode);
		if(!isOrderCode2()) {
			if(isGoodsCode) {
				order.setQuantity(1);
				order.setOrderState("P");
				if(insOrders(order)) {
					selOrderDate(order);

					order.setOrderCode(order.getEmpCode()+order.getOrderDate()+order.getCuCode());

					dao.setTransactionResult(insOrderDetail(order));
					list = selOrderDetail(order);
					Gson gson = new Gson();
					tag = gson.toJson(list);					
					
				} else { tag = "주문상품이 입력되지 않았습니다. 다시 입력해 주세요~";}
			}else { tag = "판매상품이 아닙니다.";}
		} else {
			order.setOrderCode(this.req.getParameter("orderCode"));
			order.setEmpCode(order.getOrderCode().substring(0, 3));
			order.setOrderDate(order.getOrderCode().substring(order.getEmpCode().length(), order.getEmpCode().length()+14));
			order.setCuCode(order.getOrderCode().substring(order.getEmpCode().length()+order.getOrderDate().length()));

			if(isGoodsCode) {
				order.setQuantity(1);
				boolean isInsOrderDetail = insOrderDetail(order);
				dao.setTransactionResult(isInsOrderDetail);
				if(!isInsOrderDetail);
				list = selOrderDetail(order);
				tag = makeHtmlGoodsList2(list, order.getOrderCode());
				if(!isInsOrderDetail) tag = "주문실패:주문상품이 입력되지 않았습니다. 다시 입력해 주세요~";
			} else {tag = "주문실패 : 판매상품이 아닙니다.";}


		}
		dao.setTransaction(!isGoodsCode);
		dao.dbClose();

		return tag;
	}



	private String ASearchGoods() {

		ArrayList<Order> list = null;
		tag = null;

		Order order = new Order();

		order.setEmpCode(session.getAttribute("sCode").toString());
		order.setCuCode("9999       ");
		order.setGoodsCode(this.req.getParameter("goodsCode"));
		// sales에 표현되는 부분인데 날짜는 데타베에서 불러올거니까 위의입력해야하는 정보만 order에 넣는다?

		dao = new DataAccessObject();

		boolean isGoodsCode = isGoodsCode(order);

		dao.setTransaction(!isGoodsCode);

		if(!isOrderCode2()) {

			if(isGoodsCode) {

				order.setQuantity(1);
				order.setOrderState("P");

				if(insOrders(order)) {
					selOrderDate(order);

					order.setOrderCode(order.getEmpCode()+order.getOrderDate()+order.getCuCode());

					dao.setTransactionResult(insOrderDetail(order));

					list = selOrderDetail(order);


				} else { tag = "주문상품이 입력되지 않았습니다. 다시 입력해 주세요~";}
			}else { tag = "판매상품이 아닙니다.";}
		} else {

			order.setOrderCode(this.req.getParameter("orderCode"));
			order.setEmpCode(order.getOrderCode().substring(0, 3));
			order.setOrderDate(order.getOrderCode().substring(order.getEmpCode().length(), order.getEmpCode().length()+14));
			order.setCuCode(order.getOrderCode().substring(order.getEmpCode().length()+order.getOrderDate().length()));

			if(isGoodsCode) {

				order.setQuantity(1);

				boolean isInsOrderDetail = insOrderDetail(order);
				dao.setTransactionResult(isInsOrderDetail);
				if(!isInsOrderDetail);

				list = selOrderDetail(order);
				if(!isInsOrderDetail) tag = "주문실패:주문상품이 입력되지 않았습니다. 다시 입력해 주세요~";
			} else {tag = "주문실패 : 판매상품이 아닙니다.";}
		}
		dao.setTransaction(!isGoodsCode);
		dao.dbClose();

		return tag;
	}

	private String makeHtmlGoodsList2(ArrayList<Order> list, String orderCode) {
		StringBuffer tag = new StringBuffer();
		int totalAmount = 0;

		tag.append("<table class=\"pos__ordertable\">");
		tag.append("<tr><td class=\"pos__ordercode__title\">주문코드</td><td colspan=\"4\" class=\"pos__ordercode__body\"><input type=text id=\"oCode\" name=\"orderCode\" value=\"" + orderCode + "\" readOnly /></td></tr>");
		tag.append("<tr class=\"pos__ordertable__header\">");
		tag.append("<th>상품코드</th>");
		tag.append("<th>상품명</th>");
		tag.append("<th>단가</th>");
		tag.append("<th>수량</th>");
		tag.append("<th>구매금액</th>");
		tag.append("</tr>");

		for(Order order : list) {
			tag.append("<tr onDblClick=\"callRemove(\'"+ order.getGoodsCode() + "\')\">");

			tag.append("<td>"+ order.getGoodsCode() +"</td>");
			tag.append("<td>"+ order.getGoodsName() +"</td>");
			tag.append("<td>"+ order.getGoodsPrice() +"</td>");
			tag.append("<td>"+ order.getQuantity() +"</td>");
			tag.append("<td>"+ order.getAmount() +"</td>");

			tag.append("</tr>");

			totalAmount += order.getAmount();
		}

		tag.append("<tr><td></td><td></td><td class=\"pos__ordertable__total\" colspan=\"2\">합계 금액</td><td class=\"pos__ordertable__value\">"+ totalAmount +"</td></tr>");
		tag.append("</table>");

		return tag.toString();

	}

	private boolean isOrderCode2() {
		return (this.req.getParameter("orderCode")!=null)? true:false;
	}

	private boolean isGoodsCode(Order order) {
		return dao.isGoodsCode(order);
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

}
