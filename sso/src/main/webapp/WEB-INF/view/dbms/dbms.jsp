<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인</title>
<script type="text/javascript" src="/resources/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.draggable.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.droppable.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.datagrid.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.dialog.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.panel.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.resizable.js"></script>
<script type="text/javascript" src="/resources/easyui/plugins/jquery.linkbutton.js"></script>
<script type="text/javascript" src="/resources/dbms/js/chart.js"></script>
<script type="text/javascript" src="/resources/dbms/js/dbms.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/dbms/css/reset.css">
<link rel="stylesheet" type="text/css" href="/resources/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/resources/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/dbms/css/chart.css">
<link rel="stylesheet" type="text/css" href="/resources/dbms/css/dbms.css">
<link rel="stylesheet" type="text/css" href="/resources/dbms/css/detail.css">
</head>
<script type="text/javascript">
	var dbId = '${sessionScope.JYDBID}';
	var dbPw = '${sessionScope.JYDBPW}';
	$(document).ready(function() {
		if(${sessionScope.JYDBID eq null}) {
			$('#script').val('DB ID/PW 로그인 확인 후 사용 가능합니다.');
			$('#script').attr("readonly", true);
			$('#scriptToolbar button').attr("disabled", true);
		}
		
		$.ajax({
			url: 'http://10.47.39.102:8080/dbmsTool/allSchemas',
			data : {
				dbId : dbId,
				dbPw : dbPw
			},
			dataType : 'json',
			success: function(data) {
				$('#dbmsTree').tree('loadData',data);
			}
		});
		
	})
	
	// 스키마 하위 목록 불러오기
	function getSchemaInfo(node, type) {
		if (node.children == null) {
			$.ajax({
				url: 'http://10.47.39.102:8080/dbmsTool/schemaInfo',
				data: {
					schema: node.text,
					dbId : dbId,
					dbPw: dbPw
				},
				dataType: 'json',
				success: function(data) {
					if (data == null) {
						sessionOut();
					} else {
						if(type == 'ex'){
							$('#dbmsTree').tree('toggle', node.target);
						}
						$('#dbmsTree').tree('append', {
							parent: node.target,
							data: data,
						});
						if(type == 'ex') {
							$('#dbmsTree').tree('toggle', node.target);
						}
					}
				}
			}).done(function() {
				if(type == 'db') {
					folderToggle(node);
				}
			});
		} else {
			if(type == 'db') {
				folderToggle(node);
			}
		}
	}
	
	// 오브젝트 하위 목록 불러오기
	function getObjectInfo(node, type) {
		var root = $('#dbmsTree').tree('getRoot', node.target);
		if (node.children == null) {
			$.ajax({
				url: 'http://10.47.39.102:8080/dbmsTool/objectInfo',
				data: {
					id: root.text,
					object: node.id,
					dbId : dbId,
					dbPw: dbPw
				},
				dataType: 'json',
				success: function(data) {
					if (data == null) {
						sessionOut();
					} else {
						if(type == 'ex'){
							$('#dbmsTree').tree('toggle', node.target);
						}
						$('#dbmsTree').tree('append', {
							parent: node.target,
							data: data
						});
						if(type == 'ex'){
							$('#dbmsTree').tree('toggle', node.target);
						}
					}
				}
			}).done(function() {
				if(type == 'db') {
					folderToggle(node);
				}
			});
		} else {
			if(type == 'db') {
				folderToggle(node);
			}
		}
	}
	
	// 테이블 정보 불러오기
	function loadObjectTable(node, type) {
		if(type == 'db') {
			var root = $('#dbmsTree').tree('getRoot', node.target);
			addTab(node.text, '/dbms/loadTable?schema=' + root.text + '&objectType=' + node.id + '&objectName=' + node.text
					 + '&dbId=' + dbId + '&dbPw=' + dbPw);
		}
		getTableChildren(node, type);
	}
	
	// 테이블 자식 정보 불러오기 (Column, Constraint, Index)
	function getTableChildren(node, type) {
		// 선택 노드가 Table이고 하위 항목이 없을시 실행
		if (node.id == 'TABLE' && node.children == null) {
			var root = $('#dbmsTree').tree('getRoot', node.target);
			$.ajax({
				url: 'http://10.47.39.102:8080/dbmsTool/getTableChildren',
				data: {
					schema: root.text,
					objectType: node.id,
					objectName: node.text,
					dbId : dbId,
					dbPw: dbPw
				},
				dataType: 'json',
				success: function(data) {
					if (data == null) {
						sessionOut();
					} else {
						if(type == 'ex'){
							$('#dbmsTree').tree('toggle', node.target);
						}
						$('#dbmsTree').tree('append', {
							parent: node.target,
							data: data.children
						});
						if(type == 'ex'){
							$('#dbmsTree').tree('toggle', node.target);
						}
					}
				}
			}).done(function() {
				if(type == 'db') {
					folderToggle(node);
				}
			});
		} else {
			if(type == 'db') {
				folderToggle(node);
			}
		}
	}

	// SQL 한줄 실행
	function runCurrentSQL(cursor) {
		$('#dbmsOutput').datagrid('loadData', []);
		var text = $('#script').val();
		$.ajax({
			url: 'http://10.47.39.102:8080/dbmsTool/runCurrentSQL',
			data: {
				sql: text,
				cursor: cursor,
				dbId : dbId,
				dbPw: dbPw
			},
			dataType: 'json',
			success: function(data) {
				scriptResult(data);
			},
			error: function(e) {
				sessionOut();
			}
		});
	}

	// SQL 전체 실행
	function runAllSQL() {
		$('#dbmsOutput').datagrid('loadData', []);
		var text = $('#script').val();
		$.ajax({
			url: 'http://10.47.39.102:8080/dbmsTool/runAllSQL',
			data: {
				sqls: text,
				dbId : dbId,
				dbPw: dbPw
			},
			dataType: 'json',
			success: function(dataArray) {
				for (var i = 0; i < dataArray.length; i++) {
					scriptResult(dataArray[i]);
				}
			},
			error: function(e) {
				sessionOut();
			}
		});
	}

</script>

<body id="mainBody">
	<div id="container">
		<div id="header">
			<div id="user" class="easyui-menubutton"
				data-options="menu:'#userMenu',hasDownArrow:false,showEvent:'click'">
				<img src="/resources/dbms/img/user_icon.png" />
				<div id="userLabel">
					<c:if test="${sessionScope.JYSESSION != null}">
						${sessionScope.JYSESSION}
					</c:if>
					<c:if test="${sessionScope.JYSESSION == null}">
						비회원
					</c:if>
				</div>
				<div id="userBtn">
					<span></span> <span></span> <span></span>
				</div>
			</div>
			<div id="userMenu">
				<c:if test="${sessionScope.JYSESSION != null}">
					<div onclick="window.location='/users/signOut'">로그아웃</div>
					<div id="drawerBtn">그래프</div>
					<div onclick="window.location='/users/modifyUser'">회원정보 수정</div>
					<div onclick="window.location='/users/deleteUser'">회원 탈퇴</div>
				</c:if>
				<c:if test="${sessionScope.JYSESSION == null}">
					<div onclick="window.location='/users/signIn'">로그인</div>
					<div onclick="window.location='/users/signUp'">회원가입</div>
				</c:if>
			</div>
		</div>
		<div class="easyui-layout" style="width: 100%; height: 100%;">
			<div class="side easyui-layout" data-options="region:'west',title:'Schema',split:true">
				<div class="top" data-options="region:'center',split:true">
					<ul id="dbmsTree" class="easyui-tree">
					</ul>
				</div>
				<div class="bottom" data-options="region:'south',title:'Details',split:true">
					<div id="include"></div>
				</div>
			</div>
			<c:if test="${sessionScope.JYDBID == null}">
				<div class="content easyui-layout" data-options="region:'center',title:'DB ID',split:true">
					<div class="top" data-options="region:'center',split:true">
						<div id="centerTabs" class="easyui-tabs">
							<div class="tab" title="Script" style="display: none;">
								<div id="scriptToolbar">
									<button id="runAllSQL" class="easyui-linkbutton"
										title="전체 실행 (F5)">Run All SQL</button>
									<button id="runCurrentSQL" class="easyui-linkbutton"
										title="한줄 실행 (Ctrl + Enter)">Run Current SQL</button>
								</div>
								<div id="scriptBody">
									<textarea id="script" spellcheck="false"></textarea>
								</div>
							</div>
						</div>
					</div>
					<div class="bottom" data-options="region:'south',split:true">
						<div id="console">
							<div id="consoleTabs" class="easyui-tabs" style="width: 100%;">
								<div class="tab" title="DBMS_OUTPUT" style="display: none;">
									<table id="dbmsOutput" class="easyui-datagrid"
										singleSelect="true">
										<thead>
											<tr>
												<th data-options="field:'Row',width:'100px'">Row</th>
												<th data-options="field:'DbmsOutput',width:'350px'">Dbms Output</th>
												<th data-options="field:'ExecutionTime'">Execution Time (ms)</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${sessionScope.JYDBID != null}">
				<div class="content easyui-layout" data-options="region:'center',title:'${sessionScope.JYDBID.toUpperCase()}',split:true">
					<div class="top" data-options="region:'center',split:true">
						<div id="centerTabs" class="easyui-tabs">
							<div class="tab" title="Script" style="display: none;">
								<div id="scriptToolbar">
									<button id="runAllSQL" class="easyui-linkbutton"
										title="전체 실행 (F5)">Run All SQL</button>
									<button id="runCurrentSQL" class="easyui-linkbutton"
										title="한줄 실행 (Ctrl + Enter)">Run Current SQL</button>
								</div>
								<div id="scriptBody">
									<textarea id="script" spellcheck="false"></textarea>
								</div>
							</div>
						</div>
					</div>
					<div class="bottom" data-options="region:'south',split:true">
						<div id="console">
							<div id="consoleTabs" class="easyui-tabs" style="width: 100%;">
								<div class="tab" title="DBMS_OUTPUT" style="display: none;">
									<table id="dbmsOutput" class="easyui-datagrid"
										singleSelect="true">
										<thead>
											<tr>
												<th data-options="field:'Row',width:'100px'">Row</th>
												<th data-options="field:'DbmsOutput',width:'350px'">Dbms Output</th>
												<th data-options="field:'ExecutionTime'">Execution Time (ms)</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:if>
		</div>
		<div id="drawer" class="easyui-drawer">
			<div>
				<select id="mChartCombobox" class="easyui-combobox"
					data-options="editable:false,height:'20px',width:'80px'"
					name="mChart"></select>
			</div>
			<div id="mChart">
				<canvas></canvas>
			</div>
			<div style="display: flex;">
				<select id="dChartYearCombobox" class="easyui-combobox"
					data-options="editable:false,height:'20px',width:'80px'"
					name="dChartYear"></select> &nbsp;&nbsp;&nbsp; 
				<select
					id="dChartMonthCombobox" class="easyui-combobox"
					data-options="editable:false,height:'20px',width:'80px'"
					name="dChartMonth"></select>
			</div>
			<div id="dChart">
				<canvas></canvas>
			</div>
		</div>
	</div>
</body>
</html>