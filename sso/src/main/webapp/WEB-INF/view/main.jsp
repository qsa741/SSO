<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Main</title>
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/css/signIn.css">
	
</head>
<body>
	<div class="container">
		<h1>Main</h1>
		<c:if test="${sessionScope.JYSESSION != null}">
			<input type="button" value="user modify" onclick="window.location='/users/modifyUser'">
			<input type="button" value="user delete" onclick="window.location='/users/deleteUser'">
			<input type="button" value="sign out" onclick="window.location='/users/signOut'">
			<input type="button" value="DBMS" onclick="window.location='http://localhost:8080/dbms/main'">
		</c:if>
		<button onclick="history.go(-1);">Back</button>
	</div>
</body>
</html>