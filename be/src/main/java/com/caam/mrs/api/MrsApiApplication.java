package com.caam.mrs.api;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.caam.mrs.api.service.jasper.ReportStorageService;

@Configuration
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableAsync
public class MrsApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MrsApiApplication.class, args);
	}
	
	@Bean
	ApplicationRunner init(ReportStorageService reportStorageService) {
		return args -> {
			reportStorageService.deleteAll();
			reportStorageService.init();
		};
	}
	
//	@PostConstruct void init() {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC")); }

}
