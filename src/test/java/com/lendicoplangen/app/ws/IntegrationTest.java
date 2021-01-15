package com.lendicoplangen.app.ws;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.net.URI;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lendicoplangen.app.ws.controller.PlanGeneratorController;
import com.lendicoplangen.app.ws.domain.FuturePayments;
import com.lendicoplangen.app.ws.domain.RequestPayLoad;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void futurePlanGeneratorTest() throws Exception {
		
		final String baseUrl = "http://localhost:" + port + PlanGeneratorController.BASE_URI + PlanGeneratorController.FUTURE_PAYMENTS;
        URI uri = new URI(baseUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json"); 
        headers.set("Content-Type", "application/json");
        
        ZonedDateTime startDate = ZonedDateTime.now().plusMinutes(10);
		// 23, since customer pays first installment during the begin of the loan itself.
		ZonedDateTime after24Months = startDate.plusMonths(23);
        
        RequestPayLoad requestPayLoad = RequestPayLoad.builder()
				.loanAmount(5000)
				.nominalRate(5)
				.duration(24)
				.startDate(startDate)
				.build();
        
        HttpEntity<RequestPayLoad> request = new HttpEntity<>(requestPayLoad, headers);
         
        ResponseEntity<FuturePayments> result = this.restTemplate.postForEntity(uri, request, FuturePayments.class);
        
                
        assertAll(() -> {
        	
        	assertEquals(HttpStatus.OK,  result.getStatusCode());
        	
        	FuturePayments payments = result.getBody();
        	
        	assertNotEquals(null, payments);
			
			
			assertEquals(24, payments.getBorrowerPayments().size());
			
			assertEquals("0",  payments.getBorrowerPayments()
					.get(payments.getBorrowerPayments().size() - 1)
					.getRemainingOutstandingPrincipal());
			
			assertEquals(after24Months.getMonth(), payments.getBorrowerPayments()
					.get(payments.getBorrowerPayments().size() - 1)
					.getDate().getMonth());			
			
		});         
        
	}
}
