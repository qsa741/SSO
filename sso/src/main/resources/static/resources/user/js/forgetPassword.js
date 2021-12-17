$(document).ready(function() {

	// 엔터 입력 막기
	document.addEventListener('keydown', function(event) {
		if (event.keyCode === 13) {
			event.preventDefault();
		};
	}, true);


	// id 유효성 검사
	$('#id').textbox({
		onChange: function(value) {
			$.ajax({
				url: '/users/signUpCheck',
				data: {
					id: value
				},
				success: function(data) {
					if (data != 1) {
						$('#idMsg').text('존재하지않는 아이디입니다.')
					} else {
						$('#idMsg').text('');
					}
				}
			});
		},
		inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
			keydown: function(e) {
				if (e.keyCode === 13) {
					$('#email').textbox('textbox').focus();
				}
			}
		})
	});

	// id, email 매칭여부 검사
	$('#email').textbox({
		onChange: function(value) {
			$.ajax({
				url: '/users/emailCheck',
				data: {
					id: $('#id').textbox('textbox').val(),
					email: value
				},
				success: function(data) {
					if (data != 1) {
						$('#emailMsg').text('등록된 이메일이 아닙니다.')
					} else {
						$('#emailMsg').text('');
					}
				}
			});
		},
		inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
			keydown: function(e) {
				if (e.keyCode === 13) {
					$('#forgetPasswordForm').submit();
				}
			}
		})
	});
});

function forgetPasswordSubmit() {
	var id = $('#idMsg').text();
	var email = $('#emailMsg').text();

	if (id == '존재하지않는 아이디입니다.' || !$('#id').textbox('textbox').val()) {
		alert("아이디를 입력해주세요.");
		return false;
	}
	if (email == '등록된 이메일이 아닙니다.' || !$('#email').textbox('textbox').val()) {
		alert("이메일을 입력해주세요.");
		return false;
	}

	return true;
}
