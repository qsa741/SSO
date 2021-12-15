package com.jyPage.exception.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.jyPage.exception.JYException;

@ControllerAdvice
@Controller
public class JYExceptionController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// PrintStackTrace String으로 반환
	public String getPrintStackTrace(Throwable e) {
		
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		
		return errors.toString();
	}

	// JYException 발생시 
	@ExceptionHandler(JYException.class)
	public ModelAndView handleError(JYException e) {
		ModelAndView mView = new ModelAndView();
		
		mView.addObject("exception", e);
		mView.setViewName("error/error");
		
		logger.error(getPrintStackTrace(e));
		
		return mView;
	}
	
}
