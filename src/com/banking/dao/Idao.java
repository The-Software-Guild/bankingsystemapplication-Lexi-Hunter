package com.banking.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.banking.dto.CustomerDTO;

public interface Idao {

	void saveAllCustomers(List<CustomerDTO> customers) throws IOException;
	List<CustomerDTO> retrieveAllCustomers() throws IOException, ClassNotFoundException;
}
