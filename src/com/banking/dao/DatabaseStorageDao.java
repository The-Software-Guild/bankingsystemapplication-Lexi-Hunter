package com.banking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.banking.dto.BankAccountDTO;
import com.banking.dto.CustomerDTO;
import com.banking.dto.FixedDepositAccountDTO;
import com.banking.dto.SavingsAccountDTO;

public class DatabaseStorageDao implements Idao {

	private Connection openConnection() {
		
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); // type-4 driver is registered with DriverManager
			System.out.println("MySQL Driver registered with DriverManager");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/bank", "root", "root");
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL suitable driver not found");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return con;
	}
	
	private void closeConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// JDBC API code in order to connect to underlying storage
	public void saveCustomer(CustomerDTO customer) {

	    int id = customer.getCustomerId();
	    String name = customer.getName();
	    int age = customer.getAge();
	    String mobile = customer.getMobileNumber();
	    String passport = customer.getPassportNumber();
	    LocalDate dob = customer.getDob();
	    BankAccountDTO account = customer.getAccount();

	    Connection con = openConnection();

	    try {

	        // Insert customer details
	        String sqlCustomer = "INSERT INTO customer (id, name, age, mobile, passport, dob) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name), age = VALUES(age), mobile = VALUES(mobile), passport = VALUES(passport), dob = VALUES(dob)";
	        PreparedStatement pstat = con.prepareStatement(sqlCustomer);
	        pstat.setInt(1, id);
	        pstat.setString(2, name);
	        pstat.setInt(3, age);
	        pstat.setString(4, mobile);
	        pstat.setString(5, passport);
	        Date sqlDate = java.sql.Date.valueOf(dob);
	        pstat.setDate(6, sqlDate);
	        pstat.executeUpdate();

	        if (account != null) {
	            String sqlCheck = "SELECT * FROM bank_account WHERE customer_id = ?";
	            pstat = con.prepareStatement(sqlCheck);
	            pstat.setInt(1, id);
	            ResultSet rs = pstat.executeQuery();

	            if (rs.next()) {
	                String sqlUpdateAccount = "UPDATE bank_account SET account_number = ?, bsb_code = ?, bank_name = ?, balance = ?, opening_date = ? WHERE customer_id = ?";
	                pstat = con.prepareStatement(sqlUpdateAccount);
	                pstat.setLong(1, account.getAccountNumber());
	                pstat.setLong(2, account.getBsbCode());
	                pstat.setString(3, account.getBankName());
	                pstat.setDouble(4, account.getBalance());
	                Date sqlOpeningDate = java.sql.Date.valueOf(account.getOpeningDate());
	                pstat.setDate(5, sqlOpeningDate);
	                pstat.setInt(6, id);
	                pstat.executeUpdate();
	            } else {
	                String sqlInsertAccount = "INSERT INTO bank_account (customer_id, account_number, bsb_code, bank_name, balance, opening_date) VALUES (?, ?, ?, ?, ?, ?)";
	                pstat = con.prepareStatement(sqlInsertAccount);
	                pstat.setInt(1, id);
	                pstat.setLong(2, account.getAccountNumber());
	                pstat.setLong(3, account.getBsbCode());
	                pstat.setString(4, account.getBankName());
	                pstat.setDouble(5, account.getBalance());
	                Date sqlOpeningDate = java.sql.Date.valueOf(account.getOpeningDate());
	                pstat.setDate(6, sqlOpeningDate);
	                pstat.executeUpdate();
	            }

	            if (account instanceof SavingsAccountDTO) {
	                SavingsAccountDTO savingsAccount = (SavingsAccountDTO) account;
	                String sqlCheckSavings = "SELECT * FROM savings_account WHERE customer_id = ?";
	                pstat = con.prepareStatement(sqlCheckSavings);
	                pstat.setInt(1, id);
	                rs = pstat.executeQuery();
	                
	                if (rs.next()) {
	                    String sqlUpdateSavings = "UPDATE savings_account SET is_salary_account = ?, minimum_balance = ?, interest_earned = ? WHERE customer_id = ?";
	                    pstat = con.prepareStatement(sqlUpdateSavings);
	                    pstat.setBoolean(1, savingsAccount.isSalaryAccount());
	                    pstat.setDouble(2, savingsAccount.getMinimumBalance());
	                    pstat.setDouble(3, savingsAccount.getInterestEarned());
	                    pstat.setInt(4, id);
	                    pstat.executeUpdate();
	                } else {
	                    String sqlSavingsAccount = "INSERT INTO savings_account (customer_id, is_salary_account, minimum_balance, interest_earned) VALUES (?, ?, ?, ?)";
	                    pstat = con.prepareStatement(sqlSavingsAccount);
	                    pstat.setInt(1, id);
	                    pstat.setBoolean(2, savingsAccount.isSalaryAccount());
	                    pstat.setDouble(3, savingsAccount.getMinimumBalance());
	                    pstat.setDouble(4, savingsAccount.getInterestEarned());
	                    pstat.executeUpdate();
	                }
	            } else if (account instanceof FixedDepositAccountDTO) {
	                FixedDepositAccountDTO fixedAccount = (FixedDepositAccountDTO) account;
	                String sqlCheckFixed = "SELECT * FROM fixed_deposit_account WHERE customer_id = ?";
	                pstat = con.prepareStatement(sqlCheckFixed);
	                pstat.setInt(1, id);
	                rs = pstat.executeQuery();
	                
	                if (rs.next()) {
	                    String sqlUpdateFixed = "UPDATE fixed_deposit_account SET deposit_amount = ?, tenure = ?, interest_earned = ? WHERE customer_id = ?";
	                    pstat = con.prepareStatement(sqlUpdateFixed);
	                    pstat.setDouble(1, fixedAccount.getDepositAmount());
	                    pstat.setInt(2, fixedAccount.getTenure());
	                    pstat.setDouble(3, fixedAccount.getInterestEarned());
	                    pstat.setInt(4, id);
	                    pstat.executeUpdate();
	                } else {
	                    String sqlFixedAccount = "INSERT INTO fixed_deposit_account (customer_id, deposit_amount, tenure, interest_earned) VALUES (?, ?, ?, ?)";
	                    pstat = con.prepareStatement(sqlFixedAccount);
	                    pstat.setInt(1, id);
	                    pstat.setDouble(2, fixedAccount.getDepositAmount());
	                    pstat.setInt(3, fixedAccount.getTenure());
	                    pstat.setDouble(4, fixedAccount.getInterestEarned());
	                    pstat.executeUpdate();
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    closeConnection(con);
	}

	
	@Override
	public void saveAllCustomers(List<CustomerDTO> customers) {
		// TODO Auto-generated method stub
		for(CustomerDTO c : customers) {
			saveCustomer(c);
		}
	}

	@Override
	public List<CustomerDTO> retrieveAllCustomers() {
	    List<CustomerDTO> customers = new ArrayList<>();

	    System.out.println("Getting all customers from Database:");
	    
	    Connection con = openConnection();

	    try {
	        String sql = "SELECT c.*, b.*, s.is_salary_account, s.minimum_balance, s.interest_earned as savings_interest, " +
	                "f.deposit_amount, f.tenure, f.interest_earned as fixed_interest " +
	                "FROM customer c " +
	                "LEFT JOIN bank_account b ON c.id = b.customer_id " +
	                "LEFT JOIN savings_account s ON b.customer_id = s.customer_id " +
	                "LEFT JOIN fixed_deposit_account f ON b.customer_id = f.customer_id";

	        PreparedStatement pstat = con.prepareStatement(sql);
	        ResultSet rs = pstat.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            int age = rs.getInt("age");
	            String mobile = rs.getString("mobile");
	            String passport = rs.getString("passport");
	            Date sqlDate = rs.getDate("dob");
	            LocalDate dob = sqlDate.toLocalDate();

	            BankAccountDTO account = null;
	            long accountId = rs.getLong("account_number");
	            if (!rs.wasNull()) {
	                long bsbCode = rs.getLong("bsb_code");
	                String bankName = rs.getString("bank_name");
	                double balance = rs.getDouble("balance");
	                
	                Date sqlopeningDate = rs.getDate("opening_date");
	                LocalDate openingDate = sqlopeningDate.toLocalDate();

	                boolean isSavingsAccount = rs.getBoolean("is_salary_account");
	                if (rs.wasNull()) {
	                    double depositAmount = rs.getDouble("deposit_amount");
	                    int tenure = rs.getInt("tenure");
	                    double interestEarned = rs.getDouble("fixed_interest");
	                    account = new FixedDepositAccountDTO(accountId, bsbCode, bankName, balance, openingDate, depositAmount, tenure, interestEarned);
	                } else {
	                    double minimumBalance = rs.getDouble("minimum_balance");
	                    double interestEarned = rs.getDouble("savings_interest");
	                    account = new SavingsAccountDTO(accountId, bsbCode, bankName, balance, openingDate, isSavingsAccount, minimumBalance, interestEarned);
	                }
	            }

	            CustomerDTO customer = new CustomerDTO(id, name, age, mobile, passport, dob, account);
	            customers.add(customer);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        closeConnection(con);
	    }

	    return customers;
	}
}
