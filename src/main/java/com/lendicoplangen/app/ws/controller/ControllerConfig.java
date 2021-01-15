package com.lendicoplangen.app.ws.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lendicoplangen.app.ws.service.PlanGeneratorService;

@Configuration
public class ControllerConfig {
	
	@Bean
	public PlanGeneratorController planGeneratorController(PlanGeneratorService planGeneratorService) {
		return new PlanGeneratorController(planGeneratorService);
	}

}
