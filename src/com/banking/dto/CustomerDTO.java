package com.banking.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class CustomerDTO implements Serializable{
    private static int idCounter = 100;
    private int customerId;
    private String name;
    private int age;
    private String mobileNumber;
    private String passportNumber;
    private LocalDate dob;
    private BankAccountDTO account;

    public CustomerDTO(String name, int age, String mobileNumber, String passportNumber, LocalDate dob) {
        this.customerId = idCounter++;
        this.name = name;
        this.age = age;
        this.mobileNumber = mobileNumber;
        this.passportNumber = passportNumber;
        this.dob = dob;
    }
    
    public CustomerDTO(int customerId, String name, int age, String mobileNumber, String passportNumber, LocalDate dob, BankAccountDTO account) {
        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.mobileNumber = mobileNumber;
        this.passportNumber = passportNumber;
        this.dob = dob;
        this.account = account;
    }

    public void setAccount(BankAccountDTO account) {
        this.account = account;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
    
    public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BankAccountDTO getAccount() {
        return account;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomerDTO)) {
            return false;
        }
        CustomerDTO customer = (CustomerDTO) obj;
        return this.customerId == customer.customerId;
    }
    
    @Override
    public int hashCode() {
        return customerId;
    }
    
    @Override
    public String toString() {
        String customerInfo = "Customer {" +
            "ID: " + customerId +
            ", Name: " + name +
            ", Age: " + age +
            ", Mobile: " + mobileNumber +
            ", Passport: " + passportNumber +
            ", DOB: " + dob;

        if (account == null) {
            customerInfo += ", Account: None";
        } else {
            String accountInfo =
                ", Account: {" +
                "Account Number: " + account.getAccountNumber() +
                ", BSB Code: " + account.getBsbCode() +
                ", Bank Name: " + account.getBankName() +
                ", Balance: " + account.getBalance() +
                ", Opening Date: " + account.getOpeningDate();

            if (account instanceof SavingsAccountDTO) {
                SavingsAccountDTO savingsAccount = (SavingsAccountDTO) account;
                accountInfo +=
                    ", Is Salary Account: " + savingsAccount.isSalaryAccount() +
                    ", Minimum Balance: " + savingsAccount.getMinimumBalance() +
                    ", Interest Earned: " + savingsAccount.getInterestEarned();
            } else if (account instanceof FixedDepositAccountDTO) {
                FixedDepositAccountDTO fixedAccount = (FixedDepositAccountDTO) account;
                accountInfo +=
                    ", Deposit Amount: " + fixedAccount.getDepositAmount() +
                    ", Tenure: " + fixedAccount.getTenure() +
                    ", Interest Earned: " + fixedAccount.getInterestEarned();
            }

            accountInfo += "}";
            customerInfo += accountInfo;
        }

        customerInfo += "}";
        return customerInfo;
    }


}