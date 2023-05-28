package com.banking.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.banking.controller.Controller.Storage;
import com.banking.dao.DatabaseStorageDao;
import com.banking.dao.FileStorageDao;
import com.banking.dto.CustomerDTO;
import com.banking.exceptions.CustomerNotFoundException;

public class BankService {

	private DatabaseStorageDao dbsDao;
	private FileStorageDao fsDao;
	
	public BankService() {
		dbsDao = new DatabaseStorageDao();
		fsDao = new FileStorageDao();
	}
	
	public void saveAllCustomers(List<CustomerDTO> customers, Storage storageType) throws IOException {
		if(storageType == Storage.FS) {
			fsDao.saveAllCustomers(customers);
		}else if (storageType == Storage.DB) {
			dbsDao.saveAllCustomers(customers);
		}
    }

    public List<CustomerDTO> retrieveAllCustomers(Storage storageType) throws ClassNotFoundException, IOException {
    	
    	List<CustomerDTO> customers = new ArrayList<>();
    	if(storageType == Storage.FS) {
    		customers = fsDao.retrieveAllCustomers();
		}else if (storageType == Storage.DB) {
			customers = dbsDao.retrieveAllCustomers();
		}
    	
    	return customers;
    }

	
}
