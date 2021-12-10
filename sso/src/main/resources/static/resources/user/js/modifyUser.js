$(document).ready(function() {
	// Pw 유효성 검사
	$('#pw').textbox({
		onChange: function(value) {
			pwValidate(value);
		}
	})

	// Pw와 동일 여부 확인
	$('#pwCheck').textbox({
		onChange: function(value) {
			pwCheckValidate(value);
		}
	});
	
	// Email 중복여부 확인
	$('#email').textbox({
		onChange: function(value){
    		emailValidate(value);
    	}
	});
	
	// Phone 중복 확인 
	$('#phone').textbox({
		onChange: function(value){
    		phoneValidate(value);
    	}
	});
	
	
	// Tibero DB 커넥션 테스트
	$('#dbTest').click(function(){
		var result = dbConnectionTest();
		if(result) {
			alert('DB 커넥션 성공');
		} else {
			alert('DB 커넥션 실패');
		}
	});
	
	$('#modifyUserForm').submit(function() {
		var pw = $('#pw').val();
		var pwCheck = $('#pwCheck').val();
		var email = $('#email').textbox('textbox').val();
		var phone = $('#phone').textbox('textbox').val();
		var dbId = $('#dbId').textbox('textbox').val();
        var dbPw = $('#dbPw').textbox('textbox').val();
		
		if(pwValidate(pw) == 1) {
			alert('비밀번호를 다시 확인해주세요.');
			return false;
		} else if (pwCheckValidate(pwCheck) == 1) {
			alert('비밀번호를 다시 확인해주세요.');
			return false;
		} else if(emailValidate(email) == 1) {
			alert('이메일을 다시 확인해주세요.');
			return false;
		} else if(phoneValidate(phone) == 1) {
			alert('핸드폰번호를 다시 확인해주세요.');
			return false;
		} else if(!((dbId == ' - ') && (dbPw == ' - '))) {
			if(!dbConnectionTest()) {
				alert('DB 정보를 다시 확인해주세요.');
				return false;
			}
		}
		return true;
	});
	
});

// Pw 유효성 검사
function pwValidate(value) {
	var pw = $('#pw').val();
	var pwRegExp = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$/;

	if ($('#pw').textbox('textbox').val() == '영문, 숫자, 특수문자로 8글자 이상' && value == '') {
		$('#pwMsg').text('');
		return 1;
	} else if (pw.length < 8) {
		$('#pwMsg').text('8글자 이상 입력해주세요.');
		return 1;
	} else if (pw.length > 20) {
		$('#pwMsg').text('20글자 이하로 입력해주세요.');
		return 1;
	} else if (!pwRegExp.test(value)){
		$('#pwMsg').text('영문, 숫자, 특수문자가 필수입니다.');
		return 1;
	} else if (value == '') {
		$('#pwMsg').text('비밀번호 확인을 입력해주세요.');
		return 1;
	} else {
		$('#pwMsg').text('');
		return 0;
	}
}

// pwCheck 유효성 검사
function pwCheckValidate(value) {
	var pw = $('#pw').val();
	if (pw != value) {
		$('#pwCheckMsg').text('비밀번호가 틀립니다.');
		return 1;
	} else if (pw == value) {
		$('#pwCheckMsg').text('비밀번호 확인 성공')
		return 0;
	}
}


// Email 유효성 검사
function emailValidate(value) {
	var emailRegExp = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
	
	if(value == '' || value == 'example@example.com') {
		$('#emailMsg').text('');
		return 1;
	} else if(!emailRegExp.test(value)) {
		$('#emailMsg').text('이메일 형식으로 입력해주세요.');
		return 1;
	} else {
		$.ajax({
			url : '/users/signUpCheck',
			data : {
				email : value
			},
			dataType: 'text',
			success: function(data) {
				if(data == 1) {
					$('#emailMsg').text('사용중인 이메일입니다.');
					return 1;
				} else {
					$('#emailMsg').text('사용가능한 이메일입니다.')
					return 0;
				}
			}
		});
	}
}

// Phone 유효성 검사
function phoneValidate(value) {
	var phoneRegExp = /^[0-9]/;
	
	if(value == "" || value == "'-' 제외 후 입력"){
		$('#phoneMsg').text('');
		return 1;
	} else if(!phoneRegExp.test(value)) {
		$('#phoneMsg').text('번호에는 숫자만 입력해주세요.');
		return 1;
	} else {
		$.ajax({
			url : '/users/signUpCheck',
			data : {
				phone : value
			},
			dataType: 'text',
			success: function(data) {
				if(data == 1) {
					$('#phoneMsg').text('사용중인 번호입니다.');
					return 1;
				} else {
					$('#phoneMsg').text('사용가능한 번호입니다.')
					return 0;
				}
			}
		});
	}
}

// DB ID/PW 확인
function dbConnectionTest() {
	var result = false;
	$.ajax({
		url : 'http://10.47.39.102:8080/dbmsTool/connectionTest',
		method : 'GET',
		dataType : 'json',
		data : {
			dbId : $('#dbId').textbox('textbox').val(),
			dbPw : $('#dbPw').textbox('textbox').val()
		},
		async: false,
		success : function(data) {
			result = data;
		}
	});
	
	return result;
}