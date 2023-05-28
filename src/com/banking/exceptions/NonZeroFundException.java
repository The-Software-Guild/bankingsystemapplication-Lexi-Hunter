package com.banking.exceptions;

public class NonZeroFundException extends Exception {
	public NonZeroFundException(String errorMessage) {
        super(errorMessage);
    }
}