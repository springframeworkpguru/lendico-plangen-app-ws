package com.lendicoplangen.app.ws.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class PlanGeneratorConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
	public PlanGeneratorService planGeneratorService() {
		return new PlanGeneratorServiceImpl();
	}

}
