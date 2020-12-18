<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>인증 성공</title>
<style>
#a {border:1px solid #000000; text-align:center;}
#b {text-align:center;}
</style>
<script>
//점장 : 상점관리 활성화, 알바 : 상점관리 비활성화 데이터베이스에서 원하는 데이터 추출 -> if문으로 활성화 / 비활성화

function pageLoad(){
	var level = "${empJobCode}"; //불러온 AAA는 heap에 저장되어있기 때문에 stack변수를 만들어줘야한다.
	var bt = document.getElementById("but");
	if(level=="01") {
		//버튼 활성화
		bt.disabled = false;
	}else {
		//버튼 비활성화
		bt.disabled = true;
	}
}
function main() {
	var log = document.createElement("form");
	log.setAttribute("method","post");
	log.setAttribute("action","logout");
	document.body.append(log);
	log.submit();
}

function moveSales() {
	var f = document.createElement("form");
	f.setAttribute("method","post");
	f.setAttribute("action","Sales");
	document.body.appendChild(f);
	f.submit();
}

function moveASales() {
	var form = document.createElement("form");
	form.setAttribute("method", "post");
	form.setAttribute("action","ASales");
	document.body.appendChild(form);
	form.submit();
}

function moveJsonSales(){
	var form = document.createElement("form");
	form.setAttribute("method","post");
	form.setAttribute("action","JASales")
	document.body.appendChild(form);
	form.submit();
}
</script>
</head>
<body onLoad="pageLoad()">
	<div id="a">
		<h3>${empName}님 ${lastAccessTime}이후로 간만입니다 <br><br> 반갑습니다♥</h3>
		<input type="button" value="logout" onClick="main()" />
		<!-- <input name="out" type = "hidden" value="${sCode}" /> -->
		
	</div>
	<div id="b">
		<input type="button" value="영업시작" onClick="moveSales()"/>
		<input id="but" type="button" value="상점관리" />
		<input type="button" value="ajax영업시작" onClick="moveASales()" />
		<input type="button" value="json영업시작" onClick="moveJsonSales()" />
	</div>
	${sCode}
</body>
</html>