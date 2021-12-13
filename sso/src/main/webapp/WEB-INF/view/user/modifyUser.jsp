<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>회원정보 수정</title>
	<script src="/resources/easyui/jquery.min.js"></script>
	<script src="/resources/easyui/jquery.easyui.min.js"></script>
	<script src="/resources/user/js/modifyUser.js"></script>
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/easyui/themes/color.css">
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/user/css/signUp.css">
</head>
<body>
	<div class="container">	
		<div class="header">
			회원정보 수정
		</div>
		<form action="/users/saveUser" id="modifyUserForm" method="POST">
			<div class="border">
				<div class="item">
					<div class="row2">
						<label>아이디</label>
					</div>
					${sessionScope.JYSESSION}
				</div>
				<div class="item">
					<div class="row">
						<div class="row2">
							<label>비밀번호&nbsp;<span class="essential">*</span></label>
							<div class="msg" id="pwMsg"></div>
						</div>
					</div>
					<input class="easyui-passwordbox" iconWidth="28" id="pw" name="pw" prompt="영문, 숫자, 특수문자로 8글자 이상">
				</div>
				<div class="item">
					<div class="row">
						<div class="row2">
							<label>비밀번호 확인&nbsp;<span class="essential">*</span></label>
							<div class="msg" id="pwCheckMsg"></div>
						</div>
					</div>
					<input class="easyui-passwordbox" iconWidth="28" id="pwCheck" prompt=" -">
				</div>
				<div class="item">
					<div class="row">
						<div class="row2">
							<label>이메일&nbsp;<span class="essential">*</span></label>
							<div class="msg" id="emailMsg"></div>
						</div>
					</div>
					<input class="easyui-textbox" id="email" name="email" prompt="example@example.com" value="${user.email}">
				</div>
				<div class="item">
					<div class="row">
						<div class="row2">
							<label>핸드폰번호&nbsp;<span class="essential">*</span></label>
							<div class="msg" id="phoneMsg"></div>
						</div>
					</div>
					<input class="easyui-textbox" id="phone" name="phone" prompt="'-' 제외 후 입력" value="${user.phone}">
				</div>
				<div class="item">
					<div class="row">
						<label>DB 유저이름&nbsp;</label>
					</div>
					<c:if test="${user.dbId != null || user.dbId == 'null'}">
						<input class="easyui-textbox" id="dbId" name="dbId" prompt=" - " value="${user.dbId}">
					</c:if>
					<c:if test="${user.dbId == null }">
						<input class="easyui-textbox" id="dbId" name="dbId" prompt=" - ">
					</c:if>
				</div>
				<div class="item">
					<div class="row">
						<div class="row2">
							<label>DB 비밀번호&nbsp;</label>
							<input type="button" id="dbTest" value="DB 테스트" tabindex="8">
						</div>
					</div>
					<input class="easyui-textbox" id="dbPw" name="dbPw" prompt=" - " value="${user.dbPw}">
				</div>
				<div class="signUp-btns">
					<input type="button" class="easyui-linkbutton" value="취소" onclick="history.go(-1);">
					<input type="submit" class="easyui-linkbutton" value="수정" onsubmit="signUpSubmit();">
				</div>
			</div>
		</form>
	</div>
</body>
</html>