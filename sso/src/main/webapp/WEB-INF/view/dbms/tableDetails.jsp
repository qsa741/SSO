<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$('#tableDetails').tabs({
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
				url : 'http://10.47.39.102:8080/dbmsTool/tableDetailsTable',
				data : {
					table : node.text,
					schema : schema.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					$('#tableDetailsTable').datagrid({data});
				}
			});
		}
	});
</script>
<div id="tableDetails" class="easyui-tabs">
	<div class="tab" title="Table" style="display: none;">
		<table id="tableDetailsTable" class="easyui-datagrid"
			data-options="singleSelect:'true'">
			<thead>
				<tr>
					<th data-options="field:'PARAMETER'">Parameter</th>
					<th data-options="field:'VALUE'">Value</th>
				</tr>
			</thead>
		</table>
	</div>
</div>