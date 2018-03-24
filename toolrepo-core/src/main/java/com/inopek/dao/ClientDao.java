package com.inopek.dao;

import org.springframework.data.repository.CrudRepository;

import com.inopek.beans.ClientBean;


public interface ClientDao extends CrudRepository<ClientBean, Long>,
		ClientCustomDao {

}
