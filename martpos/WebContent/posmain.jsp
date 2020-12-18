<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 작성자: 장산-->
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>POS MAIN</title>
<script src="js/custom_alert.js"></script>
<!-- javascript 는 아래에 구현-->
<script>
	
	function sendGoCode() {
		if (window.event.keyCode == 13) {
			var goodsCode = document.getElementsByName("goodsCode")[0];
			var orderCode = document.getElementsByName("orderCode")[0];

			var form = document.createElement("form");
			form.setAttribute("method", "post");
			form.setAttribute("action", "SearchGoods");

			form.appendChild(goodsCode);
			form.appendChild(orderCode);

			document.body.appendChild(form);
			form.submit();
		}
	}

	function messageAlert() {
		var message = "${message}";
		if (message != "") {
			alert("${message}");
		}
	}

	function callRemove(goCode) {
		//orderCode var orderCode = document.getElementsByName("orderCode")[0];
		var orderCode = document.getElementsByName("orderCode")[0];
		//gocode
		alert("상품코드 : " + goCode); //자바에 있는 gocode를 불러오는 function
		var input = document.createElement("input");
		input.setAttribute("type", "hidden");
		input.setAttribute("name", "goCode");
		input.setAttribute("value", goCode);

		document.body.appendChild(input);
		var goCode = document.getElementsByName("goCode")[0];

		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", "removeGoods");

		form.appendChild(orderCode);
		form.appendChild(goCode);

		document.body.appendChild(form);

		form.submit();
	}

	function payment() {
		var cuCode = document.getElementsByName("cuCode")[0];
		var form = document.createElement("form");
		var orderCode = document.getElementsByName("orderCode")[0];

		if (cuCode.value == "") {
			cuCode.focus();
			alert("고객코드를 입력하세요")
			return;
		} else {
			form.setAttribute("method", "post");
			form.setAttribute("action", "payment");
			form.appendChild(orderCode);
			form.appendChild(cuCode);
			document.body.appendChild(form);
			alert(cuCode.value + "님 결제화면으로 넘어갑니다");
			form.submit();
		}
	}
</script>
<link rel="stylesheet" href="css/app.css">
<style>
input[type="text"] {
	border: 0px solid #FFFFFF;
	width: 60%;
	height: 45px;
	text-align: center;
	font-family: 'yg-jalnan';
	color: #F15F5F;
}

#oCode {
	background-color: #f3f0ed;
	font-size: 1.8rem;
}
</style>
</head>
<body onLoad="messageAlert()">
	<div class="root">
		<div class="pos">
			<div class="pos__wrapper">
				<div class="pos__header">
					<div class="pos__title">
						<h4>San's</h4>
						<h2>convenience store</h2>
					</div>
				</div>
				<div class="pos__body">
					<div class="pos__log">
						<span class="pos__log__title">Access User</span><span
							class="pos__log__body">${empName}</span> <span
							class="pos__log__title">Access Time</span><span
							class="pos__log__body">${accessTime}</span>
					</div>
					<div class="pos__gocode">
						<span class="pos__gocode__title">상품코드</span><span
							class="pos__gocode__body"><input type="text"
							name="goodsCode" class="pos__gocode__body"
							onKeyPress="sendGoCode()" /></span>
					</div>
					<table class="pos__ordertable">
						<tr>
							<td class="pos__ordercode__title">주문코드</td>
							<td colspan="4" class="pos__ordercode__body"><input
								type=text id="oCode" name="orderCode" value="${orderCode}"
								readOnly /></td>
						</tr>
						<tr class="pos__ordertable__header">
							<th>상품코드</th>
							<th>상품명</th>
							<th>단가</th>
							<th>수량</th>
							<th>구매금액</th>
						</tr>
						<!-- tr, td 동적 생성 필요.  -->
						${goodsList}

						<tr>
							<td></td>
							<td></td>
							<td class="pos__ordertable__total" colspan="2">합계 금액</td>
							<td class="pos__ordertable__value">${totalAmount}</td>
						</tr>
					</table>
					<div class="pos__usercode">
						<span class="pos__usercode__title">고객코드</span><span
							class="pos__usercode__body"><input type="text"
							name="cuCode" class="pos__usercode__body" /></span>
					</div>
					<input type="button" class="pos__checkout" value="결제"
						onClick="payment()" />
				</div>
			</div>
		</div>
	</div>

	<div class="credit credit--pos">Designed by San</div>
</body>
</html>