package com.inopek.services.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.google.common.collect.Lists;
import com.inopek.beans.AddressBean;
import com.inopek.beans.ClientBean;
import com.inopek.dao.AddressDao;
import com.inopek.dao.ClientDao;
import com.inopek.services.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private AddressDao addressDao;

	@Override
	public ArrayList<ClientBean> findAll() {
		Iterable<ClientBean> clients = clientDao.findAll();		
		return Lists.newArrayList(clients);
	}

	@Override
	public Iterable<AddressBean> findAllAdd() {
		return addressDao.findAll();
	}
	
}
