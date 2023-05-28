package com.banking.controller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.banking.service.BankService;
import com.banking.dao.DatabaseStorageDao;
import com.banking.dao.FileStorageDao;
import com.banking.dao.Idao;
import com.banking.dto.BankAccountDTO;
import com.banking.dto.CustomerDTO;
import com.banking.dto.FixedDepositAccountDTO;
import com.banking.dto.SavingsAccountDTO;
import com.banking.exceptions.AccountAlreadyAssignedException;
import com.banking.exceptions.CustomerNotFoundException;
import com.banking.exceptions.InsufficientFundsException;
import com.banking.exceptions.NonZeroFundException;

public class Controller {
	
	public enum Storage{
		DB, FS;
	}
	
	public static void printMenu() {
		
		System.out.println("----------------------------------------------------");
		System.out.println("1. Create New Customer Data\r\n" + 
				"2. Assign a Bank Account to a Customer\r\n" + 
				"3. Display balance or interest earned of a Customer\r\n" + 
				"4. Sort Customer Data\r\n" + 
				"5. Persist Customer Data\r\n" + 
				"6. Show All Customers\r\n" + 
				"7. Search Customers by Name\r\n" +
				"8. Deposit or Withdraw\r\n" +
				"9. Exit");
	}
	
	public static int getMenuNumber() {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter your choice: ");
		while(true) {
			if(sc.hasNextInt()) {
				int num = sc.nextInt();
				if(num >= 1 && num <= 9) {
					return num;
				}else {
					System.out.println("Please enter a menu item from 1 to 9:");
				}
			}else {
				System.out.println("Please enter a menu item from 1 to 9:");
				sc.next();
			}
		}
	}
	
	public static boolean isValidDate(String date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
	    try {
	        LocalDate.parse(date, formatter);
	    } catch (DateTimeParseException e) {
	        return false;
	    }
	    return true;
	}
	
	public static CustomerDTO getCustomerById(List<CustomerDTO> customers) throws CustomerNotFoundException {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter Customer ID: ");
        int customerId;
        
		while(true) {
			if(sc.hasNextInt()) {
				customerId = sc.nextInt();
				break;

			}else {
				System.out.println("Customer ID must be an integer. Please re-enter:");
				sc.next(); // consume input
			}
		}
		
        for (CustomerDTO customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }

        throw new CustomerNotFoundException("No customer found with the id: " + customerId);
	}
	
	public static CustomerDTO getCustomerData() {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter customer name:");
		String name = sc.nextLine();
		
		System.out.println("Enter customer age:");
		
		int age;
		
		while(true) {
			if(sc.hasNextInt()) {
				age = sc.nextInt();
				if(age > 0) {
					break;
					
				}else {
					System.out.println("Age must be greater than 0. Please re-enter:");
					continue;
				}
			}else {
				System.out.println("Age must be a number, and greater than 0. Please re-enter:");
				sc.nextLine(); // consume input
			}
		}
		
		sc.nextLine(); // consume input
		
		System.out.println("Enter customer mobile number:");
		String mobileNumber = sc.nextLine();
		
		System.out.println("Enter customer passport number:");
		String passportNumber = sc.nextLine();
		
		System.out.print("Enter customer DOB (DD/MM/YYYY): ");
		String dobInput = sc.nextLine();
        // Validate DOB format
        while (!isValidDate(dobInput)) {
            System.out.println("Invalid date, please re-enter:");
            dobInput = sc.nextLine();
        }
        
     // Parse the validated date string to a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        LocalDate dob = LocalDate.parse(dobInput, formatter);
		
		return new CustomerDTO(name, age, mobileNumber, passportNumber, dob);
	}
	
	public static void assignBankAccount(ArrayList<CustomerDTO> customers) throws CustomerNotFoundException, 
	InsufficientFundsException, NonZeroFundException, AccountAlreadyAssignedException {
		
		// Make sure there is at least 1 customer, otherwise it isn't possible to assign
		if(customers.size() == 0) {
			System.out.println("There are no customers to assign bank accounts to!");
			return;
		}

		Scanner sc = new Scanner(System.in);
		
		CustomerDTO customer = getCustomerById(customers);
        
		if(customer.getAccount() != null) {
			throw new AccountAlreadyAssignedException("Bank account already assigned to that customer");
		}
		
        System.out.print("Enter Account Number: ");
        long accountNumber;
        
        while(true) {
			if(sc.hasNextLong()) {
				accountNumber = sc.nextInt();
				break;

			}else {
				System.out.println("Account Number must be a long. Please re-enter:");
				sc.next(); // consume input
			}
		}
        
        sc.nextLine();
        System.out.print("Enter BSB Code: ");
        long bsbCode;
        
        while(true) {
			if(sc.hasNextLong()) {
				bsbCode = sc.nextInt();
				break;

			}else {
				System.out.println("BSB must be a long. Please re-enter:");
				sc.next(); // consume input
			}
		}
        
        sc.nextLine(); // consume input

        System.out.print("Enter Bank Name: ");
        String bankName = sc.nextLine();
        
        
        System.out.print("Enter Account Balance: ");
        double balance;
        
        while(true) {
			if(sc.hasNextLong()) {
				balance = sc.nextDouble();
				break;

			}else {
				System.out.println("Balance must be a double. Please re-enter:");
				sc.next(); // consume input
			}
		}
        
        
        System.out.print("Enter Opening Date (DD/MM/YYYY): ");
        sc.nextLine(); // consume input
        String openingDateInput = sc.nextLine();
        // Validate Opening date format
        while (!isValidDate(openingDateInput)) {
            System.out.println("Invalid date format, please re-enter:");
            openingDateInput = sc.nextLine();
        }
        
        // Parse the validated date string to a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        LocalDate openingDate = LocalDate.parse(openingDateInput, formatter);
        
        BankAccountDTO account;

        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Fixed Deposit Account");
        int accountType;
        
		while(true) {
			if(sc.hasNextInt()) {
				accountType = sc.nextInt();
				
				if(accountType != 1 && accountType != 2) {
					System.out.println("Account type must be 1 or 2. Please re-enter:");
					continue;
				}
				break;

			}else {
				System.out.println("Account type must be an integer. Please re-enter:");
				sc.next(); // consume input
			}
		}

        if (accountType == 1) {
            System.out.print("Is Salary Account (true/false)? : ");
            boolean isSalaryAccount;
            
            while(true) {
    			if(sc.hasNextBoolean()) {
    				isSalaryAccount = sc.nextBoolean();
    				break;

    			}else {
    				System.out.println("Please write true or false:");
    				sc.next(); // consume input
    			}
    		}
            
            // If account is not a salary account, and balance is below 100, then reject the account
            
            if(isSalaryAccount && balance != 0) {
            	throw new NonZeroFundException("Balance must be zero when opening a salary account!");
            }else if(!isSalaryAccount && balance < 100) {
            	// Insufficient funds to open
            	throw new InsufficientFundsException("Not enough funds to open account! (Need at least $100)");
            }
            
            account = new SavingsAccountDTO(accountNumber, bsbCode, bankName, balance, openingDate, isSalaryAccount, 100, 0);
            
            customer.setAccount(account);
            System.out.println("Savings account assigned to customer: " + customer);
            
        } else if (accountType == 2) {
            System.out.print("Enter Deposit Amount (Min 1000): ");
            double depositAmount;
            
            while(true) {
    			if(sc.hasNextDouble()) {
    				depositAmount = sc.nextDouble();
    				
    				if(depositAmount < 1000) {
    					System.out.println("Deposit needs to be at least 1000");
    					continue;
    				}
    				break;

    			}else {
    				System.out.println("Please enter a number:");
    				sc.next(); // consume input
    			}
    		}
            
            System.out.print("Enter Tenure (in years) (Min 1 year, Max 7 years): ");
            int tenure;
            
            while(true) {
    			if(sc.hasNextInt()) {
    				tenure = sc.nextInt();
    				
    				if(tenure < 1 || tenure > 7) {
    					System.out.println("Tenure must be Min 1 year, Max 7 years");
    					continue;
    				}
    				break;

    			}else {
    				System.out.println("Please enter a number:");
    				sc.next(); // consume input
    			}
    		}
            
            
            account = new FixedDepositAccountDTO(accountNumber, bsbCode, bankName, balance, openingDate, depositAmount, tenure, 0);
            
            customer.setAccount(account);
            System.out.println("Fixed Deposit account assigned to customer: " + customer);
            
        } else {
            System.out.println("Invalid account type selection.");
        }
	}
	
	public static void displayCustomerBalanceOrInterest(ArrayList<CustomerDTO> customers) throws CustomerNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Customer ID: ");
        int requestedCustomerId;

		while(true) {
			if(sc.hasNextInt()) {
				requestedCustomerId = sc.nextInt();
				break;
				
			}else {
				System.out.println("Invalid Customer ID:");
				sc.nextLine(); // consume input
			}
		}
        
        
        boolean isCustomerFound = false;
        for (CustomerDTO customer : customers) {
            if (customer.getCustomerId() == requestedCustomerId) {
            	isCustomerFound = true;
                System.out.println(customer.getName());
                if (customer.getAccount() != null) {
                	System.out.println("Balance: " + customer.getAccount().getBalance());
                    System.out.println("Interest Earned: " + customer.getAccount().calculateInterest());
                }
                break;
            }
        }
        
        if (!isCustomerFound) {
            throw new CustomerNotFoundException("No customer found with the id: " + requestedCustomerId);
        }
        
	}
	
    public static void main(String[] args) {
    	
        Scanner sc = new Scanner(System.in);
        ArrayList<CustomerDTO> customers = new ArrayList<>();
        
        BankService bankService = new BankService();
        
        int choice;

        do {
        	printMenu();

            choice = getMenuNumber();

            switch (choice) {
                case 1:
                    // Create New Customer
                    CustomerDTO newCustomer = getCustomerData();
                    customers.add(newCustomer);
                    System.out.println("Customer created successfully: " + newCustomer);
                    break;

                case 2:
                    // Assign a Bank Account to a Customer
				try {
					assignBankAccount(customers);
				} catch (CustomerNotFoundException e1) {
					System.err.println(e1.getMessage());
				} catch (InsufficientFundsException e2) {
					System.err.println(e2.getMessage());
				} catch (NonZeroFundException e3) {
					System.err.println(e3.getMessage());
				} catch (AccountAlreadyAssignedException e4) {
					System.err.println(e4.getMessage());
				}
                    break;
                    
                case 3:
                    // Display balance or interest earned of a Customer
				try {
					displayCustomerBalanceOrInterest(customers);
				} catch (CustomerNotFoundException e1) {
					System.err.println(e1.getMessage());
				}
                    break;

                case 4:
                    // Sort Customer Data
                	// Change this to a class that is better for sorting
                	System.out.println("Select sorting type:");
                    System.out.println("1. ID");
                    System.out.println("2. Name");
                    System.out.println("3. Bank balance");
                	
                    int sortChoice;
            		
            		while(true) {
            			if(sc.hasNextInt()) {
            				sortChoice = sc.nextInt();
            				if(sortChoice >= 1 && sortChoice <= 3) {
            					break;
            					
            				}else {
            					System.out.println("Please choose options 1, 2, or 3:");
            					continue;
            				}
            			}else {
            				System.out.println("Please choose options 1, 2, or 3:");
            				sc.nextLine(); // consume input
            			}
            		}
                    
            		if(sortChoice == 1) {
            			Collections.sort(customers, Comparator.comparing(customer -> customer.getCustomerId()));
            		}else if(sortChoice == 2) {
            			Collections.sort(customers, Comparator.comparing(customer -> customer.getName()));
            		}else if(sortChoice == 3) {
            			Collections.sort(customers, Comparator.comparing(customer -> customer.getAccount().getBalance()));
            		}
            		
                    for (CustomerDTO customer : customers) {
                        System.out.println(customer);
                    }
                    break;
                    

                case 5:
                    // Persist Customer Data
                	System.out.println("Select persistence type:");
                    System.out.println("1. File System");
                    System.out.println("2. Relational Database (DBMS)");
                	
                    int persistenceChoice;
                    
            		while(true) {
            			if(sc.hasNextInt()) {
            				persistenceChoice = sc.nextInt();
            				if(persistenceChoice == 1 || persistenceChoice == 2) {
            					break;
            				}else {
            					System.out.println("Please choose between 1 or 2");
            				}
            			}else {
            				System.out.println("Please choose between 1 or 2");
            				sc.next();
            			}
            		}

            		if(persistenceChoice == 1) {
            			// Persist with File System
            			try {
							bankService.saveAllCustomers(customers, Storage.FS);
						} catch (IOException e) {
							System.err.println(e.getMessage());
						}
						
            		}else {
            			// Persist with DBMS
            			try {
							bankService.saveAllCustomers(customers, Storage.DB);
						} catch (IOException e) {
							System.err.println(e.getMessage());
						}
            		}
            		
            		break;

                case 6:
                    // Show All Customers
                	
                	System.out.println("Where should customers be retrieved from:");
                    System.out.println("1. File System");
                    System.out.println("2. Relational Database (DBMS)");
                	
                    int retrieveChoice;
                    
            		while(true) {
            			if(sc.hasNextInt()) {
            				retrieveChoice = sc.nextInt();
            				if(retrieveChoice == 1 || retrieveChoice == 2) {
            					break;
            				}else {
            					System.out.println("Please choose between 1 or 2");
            				}
            			}else {
            				System.out.println("Please choose between 1 or 2");
            				sc.next();
            			}
            		}
                	
            		List<CustomerDTO> retrievedCustomers = null;
            		
            		if(retrieveChoice == 1) {
            			try {
							retrievedCustomers = bankService.retrieveAllCustomers(Storage.FS);
						} catch (ClassNotFoundException e) {
							System.err.println(e.getMessage());
							break;
						} catch (IOException e) {
							System.err.println(e.getMessage());
							break;
						}
            		}else {
            			try {
							retrievedCustomers = bankService.retrieveAllCustomers(Storage.DB);
						} catch (ClassNotFoundException e) {
							System.err.println(e.getMessage());
							break;
						} catch (IOException e) {
							System.err.println(e.getMessage());
							break;
						}
            		}
                	
                	for(CustomerDTO c : retrievedCustomers) {
                		System.out.println(c);
                	}
            		
                    break;

                case 7:
                    // Search Customers by Name
                    try {
                        System.out.print("Enter the name of the customer: ");
                        String nameToSearch = sc.nextLine();
                        boolean isCustomerFound = false;
                        for (CustomerDTO customer : customers) {
                            if (customer.getName().equalsIgnoreCase(nameToSearch)) {
                                System.out.println(customer);
                                isCustomerFound = true;
                                break;
                            }
                        }

                        if (!isCustomerFound) {
                            throw new CustomerNotFoundException("No customer found with the name " + nameToSearch);
                        }
                    } catch (CustomerNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                    break;

                case 8:
				CustomerDTO customer = null;
				try {
					customer = getCustomerById(customers);
				} catch (CustomerNotFoundException e1) {
					System.err.println(e1.getMessage());
					break;
				}

                    // If the customer doesn't have an account, or the account is not a SavingsAccount, print an error and break
                    if (!(customer.getAccount() instanceof SavingsAccountDTO)) {
                        System.out.println("Customer does not have a Savings Account!");
                        break;
                    }

                    // At this point, we know the account is a SavingsAccount
                    SavingsAccountDTO account = (SavingsAccountDTO) customer.getAccount();

                    // Ask the user for the operation type
                    System.out.println("Enter operation type (1: Deposit, 2: Withdraw): ");
                    int operationType;
                    while (true) {
                        if(sc.hasNextInt()) {
                            operationType = sc.nextInt();
                            if(operationType != 1 && operationType != 2) {
                                System.out.println("Operation type must be 1 or 2. Please re-enter:");
                                continue;
                            }
                            break;
                        } else {
                            System.out.println("Operation type must be an integer. Please re-enter:");
                            sc.next(); // consume input
                        }
                    }

                    // Ask the user for the amount
                    System.out.println("Enter amount: ");
                    double amount;
                    while (true) {
                        if(sc.hasNextDouble()) {
                            amount = sc.nextDouble();
                            if(amount <= 0) {
                                System.out.println("Amount must be a positive number. Please re-enter:");
                                continue;
                            }
                            break;
                        } else {
                            System.out.println("Amount must be a number. Please re-enter:");
                            sc.next(); // consume input
                        }
                    }

                    // Perform the operation
                    if (operationType == 1) {
                        try {
                            account.deposit(amount);
                            System.out.println("Deposit successful. New balance: " + account.getBalance());
                        } catch (Exception e) {
                            System.out.println("Error while depositing: " + e.getMessage());
                        }
                    } else if (operationType == 2) {
                        try {
                            account.withdraw(amount);
                            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
                        } catch (InsufficientFundsException e) {
                            System.out.println("Error while withdrawing: " + e.getMessage());
                        }
                    }

                    break;
                    
                case 9:
                    // Exit
                    break;
            }

        } while (choice != 9);
        
        sc.close();
    }
}