package com.project.sso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.sso.intercept.LoginInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Value("${dbms.url}")
	private String url;

	// LoginInterceptor 인터셉터로 등록
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor(url));
	}

	// DBMS에 대한 CORS 등록
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(url).allowedMethods("*");
	}

}
