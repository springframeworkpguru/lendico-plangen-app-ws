package com.lendicoplangen.app.ws.domain;

import java.time.ZonedDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.lendicoplangen.app.ws.shared.StringConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "builder")
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayLoad {

	@NotNull
	@Min(value = 500, message = StringConstants.LOAN_AMOUNT_VALIDATION_MSG)
	private double loanAmount;
	
	@NotNull
	@Min(value = 1, message = StringConstants.NOMINAL_RATE_VALIDATION_MSG)
	private double nominalRate;
	
	@NotNull
	@Min(value = 5, message = StringConstants.DURATION_VALIDATION_MSG)
	private int duration;
	
	@NotNull
	@Future(message = StringConstants.START_DATE_VALIDATION_MSG)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.000Z")
	private ZonedDateTime startDate;
}
