package com.lendicoplangen.app.ws.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lendicoplangen.app.ws.domain.FuturePayments;
import com.lendicoplangen.app.ws.domain.RequestPayLoad;
import com.lendicoplangen.app.ws.domain.ResponsePayLoad;
import com.lendicoplangen.app.ws.shared.StringConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlanGeneratorServiceImpl implements PlanGeneratorService {

	private static final int NUMBER_OF_MONTHS_IN_A_YEAR = 12;
	private static final int MINIMUM_LOAN_AMOUNT = 500;
	private static final int MINIMUM_DURATION_IN_MONTHS = 5;
	private static final String DECIMAL_FORMATTER = "#.##";
	
	
	@Override
	public FuturePayments generatePlan(RequestPayLoad requestPayLoad) {

		if (requestPayLoad == null) {
			throw new IllegalArgumentException(StringConstants.REQUEST_PAYLOAD_ERROR);
		}
		
		double loanAmount = requestPayLoad.getLoanAmount();
		int duration = requestPayLoad.getDuration();
		double nominalRate = requestPayLoad.getNominalRate();
		ZonedDateTime startDate = requestPayLoad.getStartDate();
		
		if (loanAmount < MINIMUM_LOAN_AMOUNT || duration < MINIMUM_DURATION_IN_MONTHS || nominalRate < 1) {
			throw new IllegalArgumentException(StringConstants.INVALID_ARGS_ERROR);
		}
		
		if ( startDate == null || startDate.isBefore(ZonedDateTime.now()) ) {
			throw new IllegalArgumentException(StringConstants.START_DATE_VALIDATION_MSG);
		}

		double annuity = getAnnuity(loanAmount, nominalRate, duration);

		return getFuturePayments(requestPayLoad, annuity);
	}

	private FuturePayments getFuturePayments(RequestPayLoad requestPayLoad, double annuity) {
		double loanAmount = requestPayLoad.getLoanAmount();
		int durationInMonths = requestPayLoad.getDuration();
		double nominalRate = requestPayLoad.getNominalRate();
		ZonedDateTime startDate = requestPayLoad.getStartDate();		
		
		double remainingOutstandingPrincipal = loanAmount;
		double rateOfInterest = nominalRate / 100.0;
		double interest = 0;
		double principal = 0;
		double borrowerPaymentAmount = 0;
		double initialOutstandingPrincipal = 0;

		DecimalFormat decimalFormatter = new DecimalFormat(DECIMAL_FORMATTER);
		decimalFormatter.setRoundingMode(RoundingMode.HALF_DOWN);

		List<ResponsePayLoad> payments = new LinkedList<>();

		log.info("future payments calculation begin");
		long start = System.currentTimeMillis();
		
		while (durationInMonths >= 1) {
			interest = (rateOfInterest * 30 * remainingOutstandingPrincipal) / 360.0;
			principal = annuity - interest;
			borrowerPaymentAmount = principal + interest;
			initialOutstandingPrincipal = remainingOutstandingPrincipal;
			remainingOutstandingPrincipal = initialOutstandingPrincipal - principal;

			if (remainingOutstandingPrincipal <= 0) {
				remainingOutstandingPrincipal = 0;
			}

			payments.add(ResponsePayLoad.builder().borrowerPaymentAmount(decimalFormatter.format(borrowerPaymentAmount))
					.date(startDate)
					.initialOutstandingPrincipal(decimalFormatter.format(initialOutstandingPrincipal))
					.interest(decimalFormatter.format(interest)).principal(decimalFormatter.format(principal))
					.remainingOutstandingPrincipal(decimalFormatter.format(remainingOutstandingPrincipal)).build());
					
			startDate = startDate.plusMonths(1);
			--durationInMonths;

		}
		
		log.info("future payments calculation done, total time taken = {} ms", System.currentTimeMillis() - start);
		return FuturePayments.builder().borrowerPayments(payments).build();
	}

	private double getAnnuity(double loanAmount, double nominalRate, int duration) {

		double monthlyRateOfInterest = nominalRate / (100.0 * NUMBER_OF_MONTHS_IN_A_YEAR);
		// using http://financeformulas.net/Annuity_Payment_Formula.html as reference
		return (loanAmount * monthlyRateOfInterest) / (1.00 - Math.pow(monthlyRateOfInterest + 1, -duration));

	}

}
