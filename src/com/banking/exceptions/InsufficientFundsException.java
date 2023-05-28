package com.banking.exceptions;

public class InsufficientFundsException extends Exception {
	public InsufficientFundsException(String errorMessage) {
        super(errorMessage);
    }
}
