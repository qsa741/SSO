package com.jyPage.exception.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.jyPage.exception.JYException;

@ControllerAdvice
public class ExceptionController {
	
	// 에러 발생시 에러 메세지와 함께 에러페이지 보여주기
	@ExceptionHandler(JYException.class)
	public ModelAndView handleError(JYException e) {
		System.out.println("ERROR CODE");
		ModelAndView mView = new ModelAndView();

		mView.addObject("exception", e);
		mView.setViewName("error/error");
		
		return mView;
	}
}
