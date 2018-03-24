package com.inopek.services;

import java.util.ArrayList;

import com.inopek.beans.AddressBean;
import com.inopek.beans.ClientBean;

public interface ClientService {
	
	/**
	 * find all clients
	 * @return
	 */
	ArrayList<ClientBean> findAll();
	
	Iterable<AddressBean> findAllAdd();
	
}
