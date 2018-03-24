package com.inopek.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.inopek.beans.SinkBean;


public interface SinkDao extends CrudRepository<SinkBean, Long>, SinkCustomDao {

	@Modifying
	@Query("delete from SinkBean where id = ?1")
	void delete(Long entityId);
	
}
