package com.inopek.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.inopek.beans.SinkBean;

public interface SinkDao extends CrudRepository<SinkBean, Long>, SinkCustomDao {

	@Modifying
	@Query("delete from SinkBean where id = ?1")
	void delete(Long entityId);

	@Query( "select s from SinkBean s where s.id in :ids" )
	List<SinkBean> findSinkBeansByIds(@Param("ids") List<Long> ids);
}
