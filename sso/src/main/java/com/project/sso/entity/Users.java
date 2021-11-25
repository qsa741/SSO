package com.project.sso.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Users {
	
	@Id
	private String id;
	private String pw;
	private String email;
	private String phone;
	private String dbId;
	private String dbPw;
	private String state;
	private Date reg;
	private Date deleteReg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getDbPw() {
		return dbPw;
	}

	public void setDbPw(String dbPw) {
		this.dbPw = dbPw;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public Date getReg() {
		return reg;
	}

	public void setReg(Date reg) {
		this.reg = reg;
	}

	public Date getDeleteReg() {
		return deleteReg;
	}

	public void setDeleteReg(Date deleteReg) {
		this.deleteReg = deleteReg;
	}
	
	public Users() {
	}

	public Users(String id, String pw, String email, String phone, String dbId, String dbPw, String state, Date reg, Date deleteReg) {
		super();
		this.id = id;
		this.pw = pw;
		this.email = email;
		this.phone = phone;
		this.dbId = dbId;
		this.dbPw = dbPw;
		this.reg = reg;
		this.deleteReg = deleteReg;
		this.state = state;
	}
	
	// json 형태로 반환
	@Override
	public String toString() {
		return "{" 
				+ "\"id\":\""+id+"\","
				+ "\"pw\":\""+pw+"\","
				+ "\"email\":\""+email+"\","
				+ "\"phone\":\""+phone+"\","
				+ "\"dbId\":\""+dbId+"\","
				+ "\"dbPw\":\""+dbPw+"\","
				+ "\"reg\":\""+reg+"\","
				+ "\"deleteReg\":\""+deleteReg+"\","
				+ "\"state\":\""+state+"\""
				+ "}";
	}
}
