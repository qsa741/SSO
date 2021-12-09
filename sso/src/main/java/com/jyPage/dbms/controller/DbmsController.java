package com.jyPage.dbms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jyPage.dbms.dto.DbDTO;
import com.jyPage.dbms.dto.LoadObjectDTO;

@RequestMapping("/dbms")
@Controller
public class DbmsController {
	
	// DBMS 페이지
	@RequestMapping("/dbms")
	public String dbms() {
		return "/dbms/dbms";
	}
	
	// jsp include용 페이지 - schema
	@RequestMapping("/schemaDetails")
	public String schemaDetails() {
		return "/dbms/schemaDetails";
	}
	
	// jsp include용 페이지 - table
	@RequestMapping("/tableDetails")
	public String tableDetails() {
		return "/dbms/tableDetails";
	}
	
	// jsp include용 페이지 - index
	@RequestMapping("/indexDetails")
	public String indexDetails() {
		return "/dbms/indexDetails";
	}
	
	// jsp include용 페이지 - sequence
	@RequestMapping("/sequenceDetails")
	public String sequenceDetails() {
		return "/dbms/sequenceDetails";
	}
	
	// jsp include용 페이지 - view
	@RequestMapping("/viewDetails")
	public String viewDetails() {
		return "/dbms/viewDetails";
	}
	
	// jsp include용 페이지 - function
	@RequestMapping("/functionDetails")
	public String functionDetails() {
		return "/dbms/functionDetails";
	}
	
	// jsp include용 페이지 - function
	@RequestMapping("/packageDetails")
	public String packageDetails() {
		return "/dbms/packageDetails";
	}
	
	// jsp include용 페이지 - function
	@RequestMapping("/typeDetails")
	public String typeDetails() {
		return "/dbms/typeDetails";
	}
	
	// jsp include용 페이지 - procedure
	@RequestMapping("/procedureDetails")
	public String procedureDetails() {
		return "/dbms/procedureDetails";
	}
	
	// jsp include용 페이지 - procedure
	@RequestMapping("/triggerDetails")
	public String triggerDetails() {
		return "/dbms/triggerDetails";
	}
	
	// 추가 table tab 페이지 
	@RequestMapping("/loadTable")
	public String loadTable(LoadObjectDTO dto, DbDTO dbDto, Model model) {
		model.addAttribute("dto", dto);
		model.addAttribute("dbDto", dbDto);

		return "/dbms/table";
	}

}
