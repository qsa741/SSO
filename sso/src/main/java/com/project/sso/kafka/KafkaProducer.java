package com.project.sso.kafka;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducer {
	
	
	private static final String TOPIC = "dbmsTopic";
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Autowired
	public KafkaProducer(KafkaTemplate kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendDBMS(JSONObject json) {
		this.kafkaTemplate.send(TOPIC, json.toString());
	}
}
