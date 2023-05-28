package com.banking.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.banking.dto.CustomerDTO;

public class FileStorageDao implements Idao {

	@Override
	public void saveAllCustomers(List<CustomerDTO> customers) throws IOException {

		File file = new File("C://C353//BankingSystem//Customers//customers.txt");
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		fos = new FileOutputStream(file);
		bos = new BufferedOutputStream(fos);
		oos = new ObjectOutputStream(bos);
		oos.writeObject(customers);

		oos.close();
	}

	@Override
	public List<CustomerDTO> retrieveAllCustomers() throws IOException, ClassNotFoundException {

		File file = new File("C://C353//BankingSystem//Customers//customers.txt");
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		List<CustomerDTO> customers = new ArrayList<>();
		
		fis = new FileInputStream(file);
		bis = new BufferedInputStream(fis);
		ois = new ObjectInputStream(bis);
		ArrayList<CustomerDTO> c = (ArrayList<CustomerDTO>) ois.readObject();
		
		for(CustomerDTO customer : c) {
			customers.add(customer);
		}
			
		ois.close();
		
		return customers;
	}
	

}
