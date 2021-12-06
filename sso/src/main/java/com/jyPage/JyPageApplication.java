package com.jyPage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication
public class JyPageApplication extends SpringBootServletInitializer {

	// 외부 톰캣을 사용하기위해 사용
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JyPageApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(JyPageApplication.class, args);
	}
	
}
