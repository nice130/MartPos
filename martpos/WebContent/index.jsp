<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
:root { -
	-color-primary: #6b3434e7; -
	-color-secondary: #c42020;
}

@font-face {
	font-family: 'yg-jalnan';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_four@1.2/JalnanOTF00.woff')
		format('woff');
	font-weight: normal;
	font-style: normal;
}

/*
    브라우저 리셋 CSS
*/
* {
	box-sizing: border-box;
}
/* hidden 속성 class */
.hidden {
	display: none !important;
}

html, body {
	margin: 0px;
	padding: 0px;
	height: 100%;
	width: 100%;
	font-family: 'yg-jalnan';
	display: flex;
	justify-content: center;
	align-items: center;
}

body {
	background-color: #f5f5f5;
}

ul, li {
	margin: 0;
	margin-bottom: 1rem;
	padding: 0;
	list-style-type: none;
	/*double click hilighting deactivate*/
	-webkit-touch-callout: none;
	-webkit-user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}
/*nested list는 margin-bottom 0*/
ul ul, ul li, li ul, li li, form {
	margin-bottom: 0;
}

button {
	padding: 0;
	border: 1px solid transparent;
	outline: none;
}

button:hover {
	opacity: 0.4;
}

textarea {
	padding: 0;
}

/**/
textarea:focus, input[type="text"]:focus, input[type="password"]:focus,
	input[type="datetime"]:focus, input[type="datetime-local"]:focus, input[type="date"]:focus,
	input[type="month"]:focus, input[type="time"]:focus, input[type="week"]:focus,
	input[type="number"]:focus, input[type="email"]:focus, input[type="url"]:focus,
	input[type="search"]:focus, input[type="tel"]:focus, input[type="color"]:focus,
	.uneditable-input:focus {
	border-color: rgba(171, 188, 243, 0.8);
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px
		rgba(171, 188, 243, 0.8);
	outline: 0 none;
}

/* 버튼 */
.btn-txt {
	border: 1px solid transparent;
	transition: color .15s ease-in-out, background-color .15s ease-in-out,
		border-color .15s ease-in-out, box-shadow .15s ease-in-out;
}

.btn-txt {
	border-radius: 1rem;
	background-color: black;
	color: white;
	padding: 0.33rem 1rem;
	font: 1rem bold;
	outline: none;
	cursor: pointer;
}

.btn-txt:hover {
	background-color: var(- -color-secondary);
	opacity: 0.7;
}

.btn-txt:active {
	border: 1px solid rgba(96, 117, 185, 0.8);
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px
		rgba(151, 143, 23, 0.8);
	outline: 0 none;
}

input {
	font-size: 1rem;
	padding: .375rem .75rem;
	border-radius: .25rem;
	border: 1px solid #ced4da;
	transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
}

input:active {
	border-color: #b1ede8;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px
		rgba(156, 69, 78, 0.8);
}

.lgn-card {
	display: inline-flex;
	flex-direction: column;
	background: #fffcf9;
	box-shadow: rgba(60, 64, 67, 0.15) 0px 1px 3px 1px;
	border-width: 2px;
	border-style: initial;
	border-color: initial;
	border-image: initial;
	width: 500px;
	height: 300px;
	text-align: center;
	border-radius: 30px;
	overflow: hidden;
	margin: auto;
}

h2 {
	border-bottom: 1px solid rgba(00, 00, 00, 0.1);
	padding: 20px 0;
	margin: 0px;
	background-color: #ff6978;
	color: #ffffff;
}

section {
	display: flex;
	width: 100%;
	padding-top: 40px;
	margin: 0;
	height: 50%;
	align-items: center;
}

section>button.btn-txt {
	margin-left: auto;
}

.auth-area {
	display: flex;
	flex-flow: column;
	width: 360px;
	margin-left: 15px;
	height: 70px;
	justify-content: space-between;
	font-size: 1.3rem;
}

.auth-line {
	display: flex;
	justify-content: space-between;
}

label {
	color: #6d435a;
}

.btn-txt {
	margin-right: 15px;
	font-size: 1.5rem;
	font-family: 'yg-jalnan';
	background-color: #ff6978;
}
</style>
<script>
	function connectServer() {
		var userId = document.getElementsByName("userInfo")[0];
		var userPass = document.getElementsByName("userInfo")[1];

		var f = document.createElement("form");
		f.setAttribute("method", "post");
		f.setAttribute("action", "login");
		document.body.append(f);
		f.appendChild(userId);
		f.appendChild(userPass);

		f.submit();
	}
</script>
</head>
<body>
	<div class="lgn-card">
		<h2>
			ACCESS SYSTEM
			</h4>
			<section>
				<div class="auth-area">
					<div class="auth-line">
						<label>ID</label><input class="input-box" name="userInfo"
							placeholder="아이디">
					</div>
					<div class="auth-line">
						<label>PASSWORD</label><input class="input-box" name="userInfo"
							placeholder="비밀번호">
					</div>
				</div>
				<input type="botton" value="확인" class="btn-txt"
					onClick="connectServer()" />
			</section>
	</div>
</body>
</html>