package com.lendicoplangen.app.ws.shared;

public abstract class StringConstants {

	private StringConstants() {
		
	}
	
	//ToDo - Localization
	
	public static final String REQUEST_PAYLOAD_ERROR = "Request Payload is null";
	public static final String INVALID_ARGS_ERROR = "Invalid arguments passed to request payload.\n"
			+ "Minimum loan amount should be 500 Euros. \n"
			+ "Minimum tenure is 5 months. \n"
			+ "Minimum nominal rate is 1 percent.";
	
	
	public static final String LOAN_AMOUNT_VALIDATION_MSG = "Loan amount should be a minimum of 500 Euros";
	public static final String NOMINAL_RATE_VALIDATION_MSG = "Nominal rate should be minimum 1 percent";
	public static final String DURATION_VALIDATION_MSG = "Duration should be a minimum of 5 months";
	public static final String START_DATE_VALIDATION_MSG = "Start date should be a valid future date and time";
	public static final String INVALID_START_DATE = "Start date should be of correct format yyyy-MM-ddTHH:mm:ssZ";
	
}