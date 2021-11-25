//package com.project.sso.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//	
//	@Bean
//	public DataSource dataSource() {
//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//		dataSourceBuilder.driverClassName("com.tmax.tibero.jdbc.TbDriver");
//		dataSourceBuilder.url("jdbc:tibero:thin:@10.47.39.125:8629:DB_D_GMD");
//		dataSourceBuilder.username("tester");
//		dataSourceBuilder.password("tester");
////		DriverManagerDataSource dataSource = new DriverManagerDataSource();
////		dataSource.setDriverClassName("com.tmax.tibero.jdbc.TbDriver");
////		dataSource.setUrl("jdbc:tibero:thin:@10.47.39.125:8629:DB_D_GMD");
////		dataSource.setUsername("tester");
////		dataSource.setPassword("tester");
////		return dataSource;
//		return dataSourceBuilder.build();
//	}
//}
