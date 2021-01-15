package com.lendicoplangen.app.ws;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lendicoplangen.app.ws.domain.FuturePayments;
import com.lendicoplangen.app.ws.domain.RequestPayLoad;
import com.lendicoplangen.app.ws.domain.ResponsePayLoad;
import com.lendicoplangen.app.ws.service.PlanGeneratorService;
import com.lendicoplangen.app.ws.service.PlanGeneratorServiceImpl;
import com.lendicoplangen.app.ws.shared.StringConstants;

public class JUnitTests {

	private PlanGeneratorService planGeneratorService;

	@BeforeEach
	public void init() {
		planGeneratorService = new PlanGeneratorServiceImpl();
	}
	
	@Test
	public void requestPayLoadTest() {
		RequestPayLoad requestPayLoad = RequestPayLoad.builder().loanAmount(5000).nominalRate(5).duration(24)
				.startDate(ZonedDateTime.now()).build();
		
		assertNotNull(requestPayLoad);
		assertEquals(5000, requestPayLoad.getLoanAmount());
		assertEquals(5, requestPayLoad.getNominalRate());
		assertEquals(24, requestPayLoad.getDuration());

	}

	@Test
	public void responsePayLoadTest() {
		ResponsePayLoad responsePayLoad = ResponsePayLoad.builder()
				.borrowerPaymentAmount("219.36")
				.date(ZonedDateTime.now())
				.initialOutstandingPrincipal("5000.00")
				.interest("20.83")
				.principal("198.53")
				.remainingOutstandingPrincipal("4801.47")
				.build();
		
		assertNotNull(responsePayLoad);
		assertEquals("4801.47", responsePayLoad.getRemainingOutstandingPrincipal());
		assertEquals("198.53", responsePayLoad.getPrincipal());
		assertEquals("20.83", responsePayLoad.getInterest());
		assertEquals("5000.00", responsePayLoad.getInitialOutstandingPrincipal());
		assertEquals("219.36", responsePayLoad.getBorrowerPaymentAmount());

	}

	

	@Test
	public void futurePlanGeneratorTest() {

		ZonedDateTime startDate = ZonedDateTime.now().plusMinutes(10);
		// 23, since customer pays first installment during the begin of the loan itself.
		ZonedDateTime after24Months = startDate.plusMonths(23);
		
		RequestPayLoad requestPayLoad = RequestPayLoad.builder().loanAmount(5000).nominalRate(5).duration(24)
				.startDate(startDate).build();

		FuturePayments payments = planGeneratorService.generatePlan(requestPayLoad);

		assertAll(() -> {
			assertNotEquals(null, payments);
			
			assertEquals(24, payments.getBorrowerPayments().size());
			
			assertEquals("0",  payments.getBorrowerPayments()
					.get(payments.getBorrowerPayments().size() - 1)
					.getRemainingOutstandingPrincipal());
			
			assertEquals(after24Months.getYear(), payments.getBorrowerPayments()
					.get(payments.getBorrowerPayments().size() - 1)
					.getDate().getYear());
			
			
		});
	}
	
	@Test
	public void futurePlanGeneratorLoanAmountValidationTest() {
		
		// Minimum loan amount to be asked is 500 Euros
		RequestPayLoad requestPayLoad = RequestPayLoad.builder().loanAmount(400).nominalRate(2).duration(24)
				.startDate(ZonedDateTime.now()).build();
			
		Throwable exceptionThrown = assertThrows(IllegalArgumentException.class, () -> {
			planGeneratorService.generatePlan(requestPayLoad);
		});
		
		assertEquals(exceptionThrown.getMessage(), StringConstants.INVALID_ARGS_ERROR);
		
	}
	
	@Test
	public void futurePlanGeneratorNominalRateValidationTest() {
		// Nominal Rate should be minimum of 1%

		RequestPayLoad requestPayLoad = RequestPayLoad.builder().loanAmount(5000).nominalRate(0.75).duration(24)
				.startDate(ZonedDateTime.now()).build();
			
		Throwable exceptionThrown = assertThrows(IllegalArgumentException.class, () -> {
			planGeneratorService.generatePlan(requestPayLoad);
		});
		
		assertEquals(exceptionThrown.getMessage(), StringConstants.INVALID_ARGS_ERROR);
		
	}
	
	@Test
	public void futurePlanGeneratorDurationValidationTest() {
		
		// Minimum duration will be 5 months.

		ZonedDateTime beforeAMonth = ZonedDateTime.now().minusMonths(10);
		
		RequestPayLoad requestPayLoad = RequestPayLoad.builder().loanAmount(5000).nominalRate(5).duration(4)
				.startDate(beforeAMonth).build();
			
		Throwable exceptionThrown = assertThrows(IllegalArgumentException.class, () -> {
			planGeneratorService.generatePlan(requestPayLoad);
		});
		
		assertEquals(exceptionThrown.getMessage(), StringConstants.INVALID_ARGS_ERROR);
		
	}

	
	@Test
	public void futurePlanGeneratorstartDateValidationTest() {

		// start date for the loan should be a future date.
		ZonedDateTime beforeAMonth = ZonedDateTime.now().minusMonths(10);
		
		RequestPayLoad requestPayLoad = RequestPayLoad.builder().loanAmount(5000).nominalRate(5).duration(24)
				.startDate(beforeAMonth).build();
			
		Throwable exceptionThrown = assertThrows(IllegalArgumentException.class, () -> {
			planGeneratorService.generatePlan(requestPayLoad);
		});
		
		assertEquals(exceptionThrown.getMessage(), StringConstants.START_DATE_VALIDATION_MSG);
		
	}



	
}
