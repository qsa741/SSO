<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	$('#viewDetails').tabs({
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
			$.ajax({
				url : url + '/dbmsTool/viewDetailsColumns',
				data : {
					schema : schema.text,
					viewName : node.text,
					userId : userId
				},
				dataType: 'json',
				success : function(data) {
					$('#viewDetailsColumns').datagrid({data});
				}
			});
			$.ajax({
				url : url + '/dbmsTool/viewDetailsScript',
				data : {
					schema : schema.text,
					viewName : node.text,
					userId : userId
				},
				dataType: 'json',
				success : function(data) {
					var text = '';
					for(var i = 0; i < data.length; i++) {
						text += data[i].TEXT;
					}
					$('#viewDetailsScript').text(text);
				}
			});
		}
	});
</script>
<div id="viewDetails" class="easyui-tabs">
	<div class="tab" title="Columns" style="display:none;">
    	<table id="viewDetailsColumns" class="easyui-datagrid" data-options="singleSelect:'true'">
    		<thead>
    			<tr>
    				<th data-options="field:'COLUMN_NAME'">Column Name</th>
    				<th data-options="field:'COLUMN_ID'">Col ID</th>
    				<th data-options="field:'DATA_TYPE'">Data Type</th>
    				<th data-options="field:'NULLABLE'">Null</th>
    				<th data-options="field:'UPDATABLE'">Updatable</th>
    				<th data-options="field:'INSERTABLE'">Insertable</th>
    				<th data-options="field:'DELETABLE'">Deletable</th>
    				<th data-options="field:'COMMENTS'">Comments</th>
    			</tr>
    		</thead>
    	</table>
    </div>
	<div class="tab" title="Script" style="display:none;">
		<textarea id="viewDetailsScript" readOnly wrap="off"></textarea>
    </div>
</div>