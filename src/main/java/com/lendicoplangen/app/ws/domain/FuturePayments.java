package com.lendicoplangen.app.ws.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "builder")
@NoArgsConstructor
@AllArgsConstructor
public class FuturePayments {	
	private List<ResponsePayLoad> borrowerPayments;	
}
