package com.banking.dto;

import java.time.LocalDate;

public class FixedDepositAccountDTO extends BankAccountDTO {
    private double depositAmount;
    private int tenure;
    private double interestEarned;

    public FixedDepositAccountDTO(long accountNumber, long bsbCode, String bankName, double balance, LocalDate openingDate, double depositAmount, int tenure, double interestEarned) {
        super(accountNumber, bsbCode, bankName, balance, openingDate);
        this.depositAmount = depositAmount;
        this.tenure = tenure;
        this.interestEarned = interestEarned;
    }

    @Override
    public String toString() {
        return super.toString() + ", Deposit Amount: " + depositAmount + ", Tenure: " + tenure;
    }
    
    public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public double getInterestEarned() {
		return interestEarned;
	}

	public void setInterestEarned(double interestEarned) {
		this.interestEarned = interestEarned;
	}

	@Override
    public double calculateInterest() {
        return depositAmount * tenure * 0.08;
    }
}