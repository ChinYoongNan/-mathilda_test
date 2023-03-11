package com.caam.mrs.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "com.caam.mrs.api.schedule" })
public class ScheduleConfig {

}
