package com.banking.exceptions;

public class AccountAlreadyAssignedException extends Exception {
	public AccountAlreadyAssignedException(String errorMessage) {
        super(errorMessage);
    }
}
