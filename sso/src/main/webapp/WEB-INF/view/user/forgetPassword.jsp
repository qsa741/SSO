<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<link rel="stylesheet" type="text/css" media="screen"
	href="/resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="/resources/easyui/themes/color.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="/resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="/resources/user/css/forgetPassword.css">
</head>
<body>
	<div class="container">
		<div class="header">비밀번호 찾기</div>
		<form id="forgetPasswordForm" action="/users/sendMail" method="POST"
			onsubmit="return forgetPasswordSubmit()">
			<div class="border">
				<div class="item">
					<div class="row2">
						<label>아이디</label>
						<div class="msg" id="idMsg"></div>
					</div>
					<input class="easyui-textbox" id="id" name="id">
				</div>
				<div class="item">
					<div class="row2">
						<label>이메일</label>
						<div class="msg" id="emailMsg"></div>
					</div>
					<input class="easyui-textbox" id="email" name="email">
				</div>
				<div id="login-btns">
					<input class="easyui-linkbutton" type="button"
						onclick="window.location='/users/signIn'" value="취소"> <input
						class="easyui-linkbutton" type="submit" value="확인">
				</div>
			</div>
		</form>
	</div>
	<script src="/resources/easyui/jquery.min.js"></script>
	<script src="/resources/easyui/jquery.easyui.min.js"></script>
	<script src="/resources/user/js/forgetPassword.js"></script>
</body>
</html>