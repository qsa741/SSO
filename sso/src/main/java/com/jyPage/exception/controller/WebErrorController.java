package com.jyPage.exception.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}

	// 에러 발생시 에러 코드와 함께 에러페이지 보여주기
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		System.out.println("ERROR PAGE");
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			int statusCode = Integer.valueOf(status.toString());
			model.addAttribute("code", statusCode);
		}
		
		return "error/error";
	}
}
