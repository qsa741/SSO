<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	$('#triggerDetails').tabs({
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
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/triggerDetailsSource',
				data : {
					schema : schema.text,
					triggerName : node.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					var text = '';
					for(var i = 0; i < data.length; i++) {
						text += data[i].TEXT;
					}
					$('#triggerDetailsSource').text(text);
				}
			});
		}
	});
</script>
<div id="triggerDetails" class="easyui-tabs">
	<div class="tab" title="Source" style="display:none;">
		<textarea id="triggerDetailsSource" readOnly wrap="off"></textarea>    	
    </div>
</div>