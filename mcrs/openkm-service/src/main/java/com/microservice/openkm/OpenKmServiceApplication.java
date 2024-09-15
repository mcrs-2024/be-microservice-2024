package com.microservice.openkm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@FeignClient
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.microservice.core", "com.microservice.openkm"})
public class OpenKmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenKmServiceApplication.class, args);
	}

}
