package com.project.sso.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ScheduleSQL {
	
	// Tibero driver
	private String driver = "com.tmax.tibero.jdbc.TbDriver";
	// 125 Tibero 서버
	private String url = "jdbc:tibero:thin:@10.47.39.125:8629:DB_D_GMD";
	
	private final String DBID = "tester";
	
	private final String DBPW = "tester"; 
	
	// 가입/수정이 요청된 레코드 저장
	public List<Map<String, Object>> userScheduler() throws Exception {
		// 스케줄러가 읽지 않은 데이터 찾기
		String sql = "select * from userScheduler where readCheck = 'N'";
		// 스케줄러가 읽은 데이터 readCheck = 'Y'로 업데이트
		String sql2 = "update userScheduler set executeTime = sysdate, readCheck = 'Y' where scheduleNum = ?";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet result = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, DBID, DBPW);
			pre = conn.prepareStatement(sql);
			result = pre.executeQuery();
		
			ResultSetMetaData metaData = result.getMetaData();
			
			Map<String, Object> map;
			String col;
			while(result.next()) {
				map = new LinkedHashMap<>();
				for (int i = 0; i < 4; i++) {
					col = metaData.getColumnName(i + 1);
					map.put(col, result.getString(col));
				}
				list.add(map);
				pre = conn.prepareStatement(sql2);
				pre.setString(1, (String)map.get("SCHEDULENUM"));
				pre.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) try {conn.close();} catch(SQLException se) {}
			if(pre != null) try {conn.close();} catch(SQLException se) {}
			if(conn != null) try {conn.close();} catch(SQLException se) {}
		}
		
		return list;
		
	}
	
	// 탈퇴 후 2년이 지난 레코드 삭제
	public void deleteUserScheduler() {
		String sql = "delete from users where MONTHS_BETWEEN(sysdate, reg) >= 24 and state = 'N'";
		
		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet result = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, DBID, DBPW);
			pre = conn.prepareStatement(sql);
			result = pre.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null) try {conn.close();} catch(SQLException se) {}
			if(pre != null) try {conn.close();} catch(SQLException se) {}
			if(conn != null) try {conn.close();} catch(SQLException se) {}
		}
	}
}
