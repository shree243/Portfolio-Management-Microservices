package com.users.portfolioService.exception;

//  To catch any Exception occurs at service level 
public class PortfolioServiceException extends RuntimeException {
	public PortfolioServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}