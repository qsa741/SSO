package com.project.sso.properties;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sso")
public class Sso {
	List<Map<String, String>> properties;
	
	public Sso(List<Map<String, String>> properties) {
		this.properties = properties;
	}
	
	public List<Map<String, String>> getProperties() {
		return properties;
	}
}
