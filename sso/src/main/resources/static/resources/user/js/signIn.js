$(document).ready(function() {
	
	$('#auto').checkbox({
		value : 'auto',
		labelPosition : 'after',
		labelWidth : 200,
		width : 20,
		height : 20
		
	});
});

function signInSubmit() {
	if(!$('#id').val()) {
		alert("아이디를 입력해주세요.");
		return false;
	} else if(!$('#pw').val()) {
		alert("비밀번호를 입력해주세요.");
		return false;
	}

	return true;
}