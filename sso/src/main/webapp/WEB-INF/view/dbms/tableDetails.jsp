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
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/tableDetailsColumns',
				data : {
					table : node.text,
					schema : schema.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					$('#tableDetailsColumns').datagrid({data});
				}
			});
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/tableDetailsIndexesTop',
				data : {
					table : node.text,
					schema : schema.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					$('#tableDetailsIndexesTop').datagrid({data});
				}
			});
			$('#tableDetailsIndexesTop').datagrid({
				onSelect : function(index, row) {
					$.ajax({
						url : 'http://10.47.39.102:8080/dbmsTool/tableDetailsIndexesBottom',
						data : {
							indexName : row.INDEX_NAME,
							dbId : dbId,
							dbPw : dbPw
						},
						dataType: 'json',
						success : function(data) {
							$('#tableDetailsIndexesBottom').datagrid({data});
						}
					});
				}
			});
			$.ajax({
				url : 'http://10.47.39.102:8080/dbmsTool/tableDetailsConstraints',
				data : {
					table : node.text,
					schema : schema.text,
					dbId : dbId,
					dbPw : dbPw
				},
				dataType: 'json',
				success : function(data) {
					$('#tableDetailsConstraints').datagrid({data});
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
	<div class="tab" title="Columns" style="display: none;">
		<table id="tableDetailsColumns" class="easyui-datagrid"
			data-options="singleSelect:'true'">
			<thead>
				<tr>
					<th data-options="field:'COLUMN_NAME'">Column Name</th>
					<th data-options="field:'COLUMN_ID'">Col Id</th>
					<th data-options="field:'PK'">PK</th>
					<th data-options="field:'DATA_TYPE'">Data Type</th>
					<th data-options="field:'NULLABLE'">Null</th>
					<th data-options="field:'DATA_DEFAULT'">Default</th>
					<th data-options="field:'COMMENTS'">Comments</th>
				</tr>
			</thead>
		</table>
	</div>
	<div class="tab" title="Indexes" style="display: none;">
		<div class="easyui-layout" style="width:100%;height:100%;">
			<div data-options="region:'center',split:ture" style="height:50%;">
				<table id="tableDetailsIndexesTop" class="easyui-datagrid"
					data-options="singleSelect:'true'">
					<thead>
						<tr>
							<th data-options="field:'CONSTRAINT_NAME'">Index Name</th>
							<th data-options="field:'UNIQUENESS'">Unique</th>
							<th data-options="field:'COLUMN_NAME'">Column</th>
							<th data-options="field:'POSITION'">Position</th>
							<th data-options="field:'OWNER'">Index Owner</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'south',split:true" style="height:50%;">
				<table id="tableDetailsIndexesBottom" class="easyui-datagrid"
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
	</div>
	<div class="tab" title="Constraints" style="display: none;">
		<table id="tableDetailsConstraints" class="easyui-datagrid"
			data-options="singleSelect:'true'">
			<thead>
				<tr>
					<th data-options="field:'CONSTRAINT_NAME'">Constraint Name</th>
					<th data-options="field:'CON_TYPE'">Type</th>
					<th data-options="field:'COLUMN_NAME'">Column</th>
					<th data-options="field:'POSITION'">Position</th>
					<th data-options="field:'DELETE_RULE'">Delete Rule</th>
					<th data-options="field:'R_CONSTRAINT_NAME'">Ref Cons. Name</th>
					<th data-options="field:'SEARCH_CONDITION'">Constraint Text</th>
					<th data-options="field:'R_OWNER'">Ref Owner</th>
				</tr>
			</thead>
		</table>
	</div>
</div>