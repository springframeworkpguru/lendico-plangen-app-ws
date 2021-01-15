package com.lendicoplangen.app.ws.domain;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "builder")
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePayLoad {

	private String borrowerPaymentAmount;
	private ZonedDateTime date;
	private String initialOutstandingPrincipal;
	private String interest;
	private String principal;
	private String remainingOutstandingPrincipal;			
}
