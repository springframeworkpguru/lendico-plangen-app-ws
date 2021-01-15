package com.lendicoplangen.app.ws.service;

import com.lendicoplangen.app.ws.domain.FuturePayments;
import com.lendicoplangen.app.ws.domain.RequestPayLoad;

@FunctionalInterface
public interface PlanGeneratorService {
	
	public FuturePayments generatePlan(RequestPayLoad entity);
}
