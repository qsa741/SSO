package com.jyPage.dbms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dbms")
@Controller
public class DbmsController {

	@Value("${dbms.url}")
	private String url;

	// DBMS 페이지
	@RequestMapping("/dbms")
	public String dbms() {
		return "/dbms/dbms";
	}

	// jsp include용 페이지 - schema
	@RequestMapping("/schemaDetails")
	public String schemaDetails() {
		return "/dbms/details/schemaDetails";
	}

	// jsp include용 페이지 - table
	@RequestMapping("/tableDetails")
	public String tableDetails() {
		return "/dbms/details/tableDetails";
	}

	// jsp include용 페이지 - index
	@RequestMapping("/indexDetails")
	public String indexDetails() {
		return "/dbms/details/indexDetails";
	}

	// jsp include용 페이지 - sequence
	@RequestMapping("/sequenceDetails")
	public String sequenceDetails() {
		return "/dbms/details/sequenceDetails";
	}

	// jsp include용 페이지 - view
	@RequestMapping("/viewDetails")
	public String viewDetails() {
		return "/dbms/details/viewDetails";
	}

	// jsp include용 페이지 - function
	@RequestMapping("/functionDetails")
	public String functionDetails() {
		return "/dbms/details/functionDetails";
	}

	// jsp include용 페이지 - function
	@RequestMapping("/packageDetails")
	public String packageDetails() {
		return "/dbms/details/packageDetails";
	}

	// jsp include용 페이지 - function
	@RequestMapping("/typeDetails")
	public String typeDetails() {
		return "/dbms/details/typeDetails";
	}

	// jsp include용 페이지 - procedure
	@RequestMapping("/procedureDetails")
	public String procedureDetails() {
		return "/dbms/details/procedureDetails";
	}

	// jsp include용 페이지 - trigger
	@RequestMapping("/triggerDetails")
	public String triggerDetails() {
		return "/dbms/details/triggerDetails";
	}

}
