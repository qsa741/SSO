<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	$('#indexDetails').tabs({
	    border:false
	});
	
	$(document).ready(function() {
		if(${sessionScope.JYDBID eq null}) {
			alert('세션이 만료되었습니다. 로그인이 필요합니다.');
			window.location='/users/signOut';
		} else {
			var node = $('#dbmsTree').tree('getSelected');
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/indexDetailsIndex',
				data : {
					indexName : node.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					$('#indexDetailsIndex').datagrid({data});
				}
			});
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/indexDetailsColumns',
				data : {
					indexName : node.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					$('#indexDetailsColumns').datagrid({data});
				}
			});
		}
	});
</script>
<div id="indexDetails" class="easyui-tabs">
	<div class="tab" title="Index" style="display:none;">
    	<table id="indexDetailsIndex" class="easyui-datagrid" data-options="singleSelect:'true'">
    		<thead>
    			<tr>
    				<th data-options="field:'PARAMETER'">Parameter</th>
    				<th data-options="field:'VALUE'">Value</th>
    			</tr>
    		</thead>
    	</table>
    </div>
	<div class="tab" title="Columns" style="display:none;">
    	<table id="indexDetailsColumns" class="easyui-datagrid" data-options="singleSelect:'true'">
    		<thead>
    			<tr>
    				<th data-options="field:'COLUMN_NAME'">Column</th>
    				<th data-options="field:'COLUMN_POSITION'">Position</th>
    				<th data-options="field:'COLUMN_LENGTH'">Length</th>
    				<th data-options="field:'TABLE_OWNER'">Table Owner</th>
    				<th data-options="field:'TABLE_NAME'">Table Name</th>
    			</tr>
    		</thead>
    	</table>
    </div>
</div>