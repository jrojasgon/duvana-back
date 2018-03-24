package com.inopek.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.inopek.beans.AddressBean;


@Transactional
public interface AddressDao extends CrudRepository<AddressBean, Long> {
	
}
