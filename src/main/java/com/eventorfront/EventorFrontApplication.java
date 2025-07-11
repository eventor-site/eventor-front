package com.eventorfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableAspectJAutoProxy
@SpringBootApplication
public class EventorFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventorFrontApplication.class, args);
	}

}
