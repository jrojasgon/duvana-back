package com.inopek.dao;

import org.springframework.data.repository.CrudRepository;

import com.inopek.beans.UserBean;


public interface UserDao extends CrudRepository<UserBean, Long>, UserCustomDao {

}
