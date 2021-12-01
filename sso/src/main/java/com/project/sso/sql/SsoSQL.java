package com.project.sso.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SsoSQL {
	
	// Tibero driver, url, username, password
	@Value("${spring.datasource.driver-class-name}")
	private String driver;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	// 유저활동 저장
	public void saveUserAction(String action, String id) throws Exception {
		String sql = "insert into userAction values(?,?,sysdate)";
		
		Connection conn = null;
		PreparedStatement pre = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			pre = conn.prepareStatement(sql);
			pre.setString(1, action);
			pre.setString(2, id);
			pre.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pre != null) try {pre.close();} catch(SQLException se) {}
			if(conn != null) try {conn.close();} catch(SQLException se) {}
		}
		
	}
	
	// 전날 userAction 내역 액션별로 카운트
	public JSONObject getUserAction(String action) {
		JSONObject json = new JSONObject();
		
		String sql = "select to_char(reg, 'YYYY-MM-DD') as reg, count(*) as count "
				+ "from (select * from userAction where action = ? and to_char(reg, 'YYYY-MM-DD') = to_char(sysdate - 1, 'YYYY-MM-DD')) "
				+ "group by to_char(reg, 'YYYY-MM-DD')";
		
		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			pre = conn.prepareStatement(sql);
			pre.setString(1, action);
			rs = pre.executeQuery();
			
			while(rs.next()) {
				String[] dateArray = rs.getString(1).split("-");
				json.put("year", dateArray[0]);
				json.put("month", dateArray[1]);
				json.put("day", dateArray[2]);
				json.put("count", rs.getInt(2));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();} catch(SQLException se) {}
			if(pre != null) try {pre.close();} catch(SQLException se) {}
			if(conn != null) try {conn.close();} catch(SQLException se) {}
		}
		
		return json;
	}
	
	// actionScheduler 테이블에 명령어 등록
	public void actionSchedulerSave(String data) {
		String sql = "insert into ACTIONSCHEDULER values(GRAPH_SEQ.NEXTVAL, ?, sysdate, 'N')";
		
		Connection conn = null;
		PreparedStatement pre = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			pre = conn.prepareStatement(sql);
			pre.setString(1, data);
			pre.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pre != null) try {pre.close();} catch(SQLException se) {}
			if(conn != null) try {conn.close();} catch(SQLException se) {}
		}
	}

}
