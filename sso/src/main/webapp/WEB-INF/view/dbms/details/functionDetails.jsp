<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	$('#functionDetails').tabs({
	    border:false
	});
	
	$(document).ready(function() {
		var url = 'http://10.47.39.102:8080';
		if(${sessionScope.JYDBID eq null}) {
			alert('세션이 만료되었습니다. 로그인이 필요합니다.');
			window.location='/users/signOut';
		} else {
			var node = $('#dbmsTree').tree('getSelected');
			var parent = $('#dbmsTree').tree('getParent', node.target);
			var schema = $('#dbmsTree').tree('getParent', parent.target);
			schema = $('#dbmsTree').tree('getParent', schema.target);
			$.ajax({
				url : url + '/dbmsTool/functionDetailsCode',
				data : {
					schemaName : schema.text,
					functionName : node.text,
					objectType : 'FUNCTION',
					userId : userId
				},
				dataType: 'json',
				success : function(data) {
					var text = '';
					for(var i = 0; i < data.length; i++) {
						text += data[i].TEXT;
					}
					$('#functionDetailsCode').text(text);
				}
			});
		}
	});
</script>
<div id="functionDetails" class="easyui-tabs">
	<div class="tab" title="Code" style="display: none;">
		<textarea id="functionDetailsCode" readOnly wrap="off"></textarea>
	</div>
</div>