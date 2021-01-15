package com.lendicoplangen.app.ws.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lendicoplangen.app.ws.domain.FuturePayments;
import com.lendicoplangen.app.ws.domain.RequestPayLoad;
import com.lendicoplangen.app.ws.service.PlanGeneratorService;

@RequestMapping(PlanGeneratorController.BASE_URI)
public class PlanGeneratorController {
	public static final String BASE_URI = "/lendico";
	public static final String FUTURE_PAYMENTS = "/future/payments";
	
	private final PlanGeneratorService planGeneratorService;
	
	public PlanGeneratorController(final PlanGeneratorService planGeneratorService) {
		this.planGeneratorService = planGeneratorService;
	}
	
	@PostMapping(path = FUTURE_PAYMENTS, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<FuturePayments> createPayments(@Valid @RequestBody RequestPayLoad requestEntity) {
		
		return new ResponseEntity<>(planGeneratorService.generatePlan(requestEntity), HttpStatus.OK);
	}

}
