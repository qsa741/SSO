<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	$('#procedureDetails').tabs({
	    border:false
	});
	
	$(document).ready(function() {
		if(${sessionScope.JYDBID eq null}) {
			alert('세션이 만료되었습니다. 로그인이 필요합니다.');
			window.location='/users/signOut';
		} else {
			var node = $('#dbmsTree').tree('getSelected');
			var parent = $('#dbmsTree').tree('getParent', node.target);
			var schema = $('#dbmsTree').tree('getParent', parent.target);
			schema = $('#dbmsTree').tree('getParent', schema.target);
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/procedureDetailsCode',
				data : {
					schema : schema.text,
					procedureName : node.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					var text = '';
					for(var i = 0; i < data.length; i++) {
						text += data[i].TEXT;
					}
					$('#procedureDetailsCode').text(text);
				}
			});
		}
	});
</script>
<div id="procedureDetails" class="easyui-tabs">
	<div class="tab" title="Code" style="display:none;">
		<textarea id="procedureDetailsCode" readOnly wrap="off"></textarea>    	
    </div>
</div>