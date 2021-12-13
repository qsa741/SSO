var userId;
$(document).ready(function() {
	userId = $('#userLabel label').text();

	// dbmsTree 초기 데이터 세팅
	$.ajax({
		url : 'http://10.47.39.102:8080/dbmsTool/allSchemas',
		data : {
			userId : userId
		},
		dataType : 'json'
	}).done(function (data){
		$('#dbmsTree').tree('loadData',data);
	})
	
	// 이전 클릭 위치 저장
	var saveClick;
	// 기본 세팅 및 클릭 이벤트
	$('#dbmsTree').tree({
		height: '100%',
		// 클릭시 ID에 맞는 디테일 정보 불러오기
		onClick: function(node) {
			if (node.id == 'SCHEMA' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load("/dbms/schemaDetails");
			} else if ((node.id == 'TABLE' || node.id == 'COLUMN') && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/tableDetails');
			} else if (node.id == 'INDEX' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/indexDetails');
			} else if (node.id == 'SEQUENCE' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/sequenceDetails');
			} else if (node.id == 'VIEW' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/viewDetails');
			} else if (node.id == 'FUNCTION' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/functionDetails');
			} else if (node.id == 'PACKAGE' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/packageDetails');
			} else if (node.id == 'TYPE' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/typeDetails');
			} else if (node.id == 'PROCEDURE' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/procedureDetails');
			} else if (node.id == 'TRIGGER' && node.text != saveClick) {
				$('#include').empty();
				$('#include').load('/dbms/triggerDetails');
			}
			saveClick = node.text;
			
		},
		// 더블클릭시 ID에 맞는 하위 목록 불러오기
		onDblClick: function(node) {
			if (node.id == 'SCHEMA') {
				getSchemaInfo(node,'db');
			} else if (node.id == 'TABLE') {
				loadObjectTable(node, 'db');
			} else {
				getObjectInfo(node, 'db');
			}
		},
		// 화살표 클릭시 ID에 맞는 하위 목록 불러오기
		onBeforeExpand: function(node) {
			if (node.id == 'SCHEMA') {
				getSchemaInfo(node, 'ex');
			} else if (node.id == 'TABLE') {
				loadObjectTable(node, 'ex');
			} else {
				getObjectInfo(node, 'ex');
			}
		}
		
	});
	
	// 중앙 탭 높이 설정
	$('#centerTabs').tabs({
		height: '100%'
	});

	// DBMS 중앙 TEXTAREA 단축키 지정
	$('#script').keydown(function(e) {
		// tab키 입력
		if (e.keyCode === 9) {
			// 커서의 위치 저장
			var start = this.selectionStart;
			var end = this.selectionEnd;

			var $this = $(this);
			var value = $this.val();

			// 시작 위치와 끝 위치 사이에 '\t' 넣기
			$this.val(value.substring(0, start)
				+ "\t"
				+ value.substring(end));

			// 커서 위치 탭 다음으로 바꾸기
			this.selectionStart = this.selectionEnd = start + 1;

			// 기본 이벤트 막기
			e.preventDefault();
		}
		// Ctrl + Enter 입력
		if (e.keyCode == 13 && e.ctrlKey) {
			runCurrentSQL(this.selectionEnd);
		}
		// F5 입력
		if (e.keyCode == 116) {
			runAllSQL();
			e.preventDefault();
		}
	});
	
	// 마우스 클릭시 커서 위치 저장
	var cursor = 0;
	$('#script').click(function() {
		cursor = this.selectionEnd;
	});

	// 마지막으로 선택된 SQL 한줄 실행
	$('#runCurrentSQL').click(function() {
		runCurrentSQL(cursor);
	});

	// SQL 전체 실행
	$('#runAllSQL').click(function() {
		runAllSQL();
	});

	// 차트 초기값 세팅
	setChartYears();

	// 차트 콤보박스 Change 이벤트 세팅
	$('#mChartCombobox').combobox({
		onChange: function(data) {
			selectMChartYear(data);
		}
	});
	$('#dChartYearCombobox').combobox({
		onChange: function(data) {
			selectDChartYear(data);
		}
	});
	$('#dChartMonthCombobox').combobox({
		onChange: function(data) {
			selectDChartMonth(data);
		}
	});


	// 그래프 Drawer css 
	$('#drawerBtn').click(function() {
		$('.drawer').addClass('chartDrawer');	
		$('#drawer').drawer('expand');
	});
	$('.drawer-mask').click(function() {
		$('.drawer').removeClass('chartDrawer');
	})

});
// 스키마 하위 목록 불러오기
function getSchemaInfo(node, type) {
	if (node.children == null) {
		$.ajax({
			url: 'http://10.47.39.102:8080/dbmsTool/schemaInfo',
			data: {
				schema: node.text,
				userId : userId
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
				userId: userId
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
		addTab(node.text, '/dbms/loadTable?schema=' + root.text + '&objectType=' + node.id + '&objectName=' + node.text + '&userId=' + userId);
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
				userId: userId
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
			userId: userId
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
			userId: userId
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

// 중앙 div 탭 추가하기
function addTab(title, url) {
	// 탭이 존재하면 선택하기
	if ($('#centerTabs').tabs('exists', title)) {
		$('#centerTabs').tabs('select', title);
	} else {
		// html include 방식으로 구현
		var content = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
		$('#centerTabs').tabs('add', {
			title: title,
			content: content,
			closable: true,
		});
	}
}


// 열린 폴더 닫기
function folderToggle(node) {
	$('#dbmsTree').tree('toggle', node.target);
}

// Script 결과 출력
function scriptResult(data) {
	// SQL SELECT문 결과가 존재
	if (data.size != 0) {
		consoleAddTab(data);
	// 나머지 경우
	} else {
		dbmsOutput(data.data);
	}
}

// SELECT 검색 시 콘솔 탭 추가
function consoleAddTab(data) {
	// 같은 검색일 시 삭제 후 실행
	if ($('#consoleTabs').tabs('exists', data.sql)) {
		$('#consoleTabs').tabs('close', data.sql);
		consoleAddTab(data);
	} else {
		$('#consoleTabs').tabs('add', {
			title: data.sql,
			content: '<div id="' + data.key + '" class="easyui-datagrid" singleSelect="true" style="width:100%;height:100%;"></div>',
			closable: true
		});
		var columns = []
		var obj = data.cols;
		for (var value in obj) {
			var menuItem = {
				field: obj[value],
				title: obj[value],
				width: 100,
			};
			columns.push(menuItem);
		}
		$('#' + data.key).datagrid({
			columns: [columns],
			data: data.data
		});
	}
}

// DBMS OUTPUT 메세지가 존재할 시 DBMS_OUTPUT 텍스트 추가
function dbmsOutput(data) {
	$('#consoleTabs').tabs('select', 'DBMS_OUTPUT');
	$('#dbmsOutput').datagrid('appendRow', {
		Row: data[0].Row,
		DbmsOutput: data[0].DbmsOutput,
		ExecutionTime: data[0].ExecutionTime
	});
}

// 세션이 사라지면 로그인페이지로 유도
function sessionOut() {
	alert('세션이 만료되었습니다. 로그인이 필요합니다.');
	window.location = '/users/signOut';
}

// ActionData 테이블에 존재하는 연도 가져오기
function setChartYears() {
	$.ajax({
		url: 'http://10.47.39.102:8080/dbmsTool/getChartYears',
		success: function(data) {
			var values = $('#mChartCombobox').combobox('getData');
			for (var i = 0; i < data.length; i++) {
				values.push({ value: data[i], text: data[i] });
				if (i == 0) {
					selectMChartYear(data[i]);
					selectDChartYear(data[i]);
				}
			}
			$('#mChartCombobox').combobox('loadData', values);
			$('#dChartYearCombobox').combobox('loadData', values);
		}
	});
}

// ActionData 테이블에 존재하는 연도별 월 가져오기
function setChartMonth(year) {
	$.ajax({
		url: 'http://10.47.39.102:8080/dbmsTool/getChartMonth',
		data: {
			year: year
		},
		dataType: 'json',
		success: function(data) {
			$('#dChartMonthCombobox').combobox('loadData',[]);
			var values = $('#dChartMonthCombobox').combobox('getData');
			for (var i = 0; i < data.length; i++) {
				values.push({ value: data[i], text: data[i] });
				if (i == 0) {
					selectDChartMonth(data[i]);
				}
			}
			$('#dChartMonthCombobox').combobox('loadData', values);
		}
	});
}

// mChart에서 연도 선택시 실행
function selectMChartYear(year) {
	$('#mChartCombobox').combobox('setValue', year);
	$('#mChartCombobox').combobox('setText', year);
	setMChart(year);
}

// dChart에서 연도 선택시 실행
function selectDChartYear(year) {
	$('#dChartYearCombobox').combobox('setValue', year);
	$('#dChartYearCombobox').combobox('setText', year);
	setChartMonth(year);
	var month = $('#dChartMonthCombobox').combobox('getValue');
	setDChart(year, month);
}

// dChart에서 월 선택시 실행
function selectDChartMonth(month) {
	$('#dChartMonthCombobox').combobox('setValue', month);
	$('#dChartMonthCombobox').combobox('setText', month);
	var year = $('#dChartYearCombobox').combobox('getValue');
	setDChart(year, month);

}

// mChart 정보 불러오기
function setMChart(year) {
	$.ajax({
		url: 'http://10.47.39.102:8080/dbmsTool/setMChart',
		data: {
			year: year
		},
		dataType: 'json',
		success: function(data) {
			var config = {
				type: 'line',
				data: data,
				options: {
					maintainAspectRatio: false,
					plugins: {
						title: {
							display: true,
							text: '사용자 월별 통계'
						}
					},
					scales: {
						x: {
							title: {
								display: true,
								text: '월별'
							}
						},
						y: {
							title: {
								display: true,
								text: '카운트'
							}
						}
					}
				}
			}
			$('#mChart').empty();
			$('#mChart').append('<canvas></canvas>');
			new Chart($('#mChart canvas'), config);
		}
	});
}

// dChart 정보 불러오기
function setDChart(year, month) {
	$.ajax({
		url: 'http://10.47.39.102:8080/dbmsTool/setDChart',
		data: {
			year: year,
			month: month
		},
		dataType: 'json',
		success: function(data) {
			var config = {
				type: 'line',
				data: data,
				options: {
					maintainAspectRatio: false,
					plugins: {
						title: {
							display: true,
							text: '사용자 일별 통계'
						}
					},
					scales: {
						x: {
							title: {
								display: true,
								text: '일별'
							}
						},
						y: {
							title: {
								display: true,
								text: '카운트'
							}
						}
					}
				}
			}
			$('#dChart').empty();
			$('#dChart').append('<canvas></canvas>');
			new Chart($('#dChart canvas'), config);
		}
	});
}
