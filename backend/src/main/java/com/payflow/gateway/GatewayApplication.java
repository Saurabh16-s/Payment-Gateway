package com.payflow.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication{
	public static void main(String[] args){
		System.setProperty("user.timezone","UTC");
		SpringApplication.run(GatewayApplication.class,args);
	}
}