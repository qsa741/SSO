<link rel="stylesheet" type="text/css" href="/resources/dbms/css/reset.css">
<link rel="stylesheet" type="text/css" href="/resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/dbms/css/dbms.css">
<script type="text/javascript" src="/resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.datagrid.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.panel.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$.ajax({
		url : 'http://10.47.39.102:8080/dbmsTool/loadObject',
		data : {
			schema : '${dto.schema}',
			objectType : '${dto.objectType}',
			objectName : '${dto.objectName}',
			dbId : '${dbDto.dbId}',
			dbPw : '${dbDto.dbPw}'
		},
		dataType : 'json',
		success :  function(data) {
			var cols = []
			var obj = data.columns;
			for (var value in obj) {
				var menuItem = {
					field: obj[value],
					title: obj[value],
					width: 100,
					align: 'center'
				};
				cols.push(menuItem);
			} 
			$('#dbmsDatagrid').datagrid({
				columns : [cols],
				data : data.data
			});
		} 
	});
});
</script>
<table class="easyui-datagrid" id="dbmsDatagrid" singleSelect="true" style="width:;height:100%;overflow:auto;">
</table>