package com.banking.dto;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class BankAccountDTO implements Serializable{
    private long accountNumber;
    private long bsbCode;
    private String bankName;
    protected transient double balance;
    private LocalDate openingDate;

    public BankAccountDTO(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate) {
        this.accountNumber = accountNumber;
        this.bsbCode = bsbCode;
        this.bankName = bankName;
        this.balance = balance;
        this.openingDate = openingDate;
    }

    public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getBsbCode() {
		return bsbCode;
	}

	public void setBsbCode(long bsbCode) {
		this.bsbCode = bsbCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public LocalDate getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}

	public abstract double calculateInterest();

    @Override
    public String toString() {
        return "Account Number: " + accountNumber + ", BSB Code: " + bsbCode + ", Bank Name: " + bankName + ", Balance: " + balance + ", Opening Date: " + openingDate;
    }
}