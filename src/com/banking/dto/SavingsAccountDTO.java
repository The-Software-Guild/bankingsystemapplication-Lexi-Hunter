package com.banking.dto;

import java.time.LocalDate;

import com.banking.exceptions.InsufficientFundsException;

public class SavingsAccountDTO extends BankAccountDTO {

	private boolean isSalaryAccount;
    private double minimumBalance;
    private double interestEarned;
    
    public SavingsAccountDTO(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate, boolean isSalaryAccount, double minimumBalance, double interestEarned) {
        super(accountNumber, bsbCode, bankName, balance, openingDate);

        this.isSalaryAccount = isSalaryAccount;
        this.minimumBalance = minimumBalance;
        this.interestEarned = interestEarned;
        
    }
    
    public void deposit(double amount) {
        if(amount > 0) {
            this.setBalance(this.getBalance() + amount);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if(isSalaryAccount() && this.getBalance() - amount < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        } else if(!isSalaryAccount() && this.getBalance() - amount < this.getMinimumBalance()) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal, minimum balance should be retained");
        } else {
            this.setBalance(this.getBalance() - amount);
        }
    }

    public double checkBalance() {
        return this.getBalance();
    }
    
    public boolean isSalaryAccount() {
		return isSalaryAccount;
	}

	public void setSalaryAccount(boolean isSalaryAccount) {
		this.isSalaryAccount = isSalaryAccount;
	}

	public double getMinimumBalance() {
		return minimumBalance;
	}

	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	public double getInterestEarned() {
		return interestEarned;
	}

	public void setInterestEarned(double interestEarned) {
		this.interestEarned = interestEarned;
	}

	@Override
    public double calculateInterest() {
        return balance * 0.04;
    }
}