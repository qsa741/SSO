<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>에러</title>
<script type="text/javascript" src="/resources/easyui/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/error/css/reset.css">
<link rel="stylesheet" type="text/css" href="/resources/error/css/error.css">
</head>
<body>
	<div class="msg">
		<h2>${code} ERROR</h2>
		<button onclick="window.location='/users/signOut'">로그인 페이지로 이동</button>
	</div>
</body>
</html>