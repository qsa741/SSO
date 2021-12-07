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
	
	// 추가 table tab 페이지 
	@RequestMapping("/loadTable")
	public String loadTable(LoadObjectDTO dto, DbDTO dbDto, Model model) {
		model.addAttribute("dto", dto);
		model.addAttribute("dbDto", dbDto);

		return "/dbms/table";
	}

}
