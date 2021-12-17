<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="EUC-KR">
	<title>로그인</title>
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/easyui/themes/color.css">
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="/resources/user/css/signIn.css">
</head>
<script type="text/javascript">
	if(${warning == 0}) {
		alert("존재하지 않는 아이디입니다.");
		window.location='/users/signIn';
	} else if(${warning == 2}) {
		alert("비밀번호가 틀렸습니다.");
		window.location='/users/signIn';
	} else if(${warning == 3}) {
		alert("탈퇴된 계정입니다.");
		window.location='/users/signIn';
	}
</script>
<body>
	<div class="container">
		<div class="header">
			로그인
		</div>
			<form action="/users/signInCheck" method="POST" id="signInForm" onsubmit="return signInSubmit()">
				<div class="border">
					<div class="item">
						<div class="row">
							<label>아이디</label>
							<input class="easyui-textbox" name="id" id="id" tabindex="1">
						</div>
					</div>
					<div class="item">
						<div class="row">
							<div class="row2">
								<label>비밀번호</label>
								<a href="/users/forgetPassword" tabindex="3">비밀번호 찾기</a>
							</div>
							<input class="easyui-passwordbox" name="pw" id="pw" iconWidth="28" tabindex="2">
						</div>
					</div>
					<div class="row">
						<input class="easyui-checkbox" label="자동 로그인" name="auto" id="auto">
					</div>
					<div id="login-btns">
						<input class="easyui-linkbutton" type="button" onclick="window.location='/users/signUp'" value="회원가입">
						<input class="easyui-linkbutton" type="submit" value="로그인">
					</div>
				</div>
			</form>
		</div>
	</div>
	<script src="/resources/easyui/jquery.min.js"></script>
	<script src="/resources/easyui/jquery.easyui.min.js"></script>
	<script src="/resources/user/js/signIn.js"></script>
</body>
</html>