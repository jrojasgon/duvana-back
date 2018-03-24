package com.inopek.dao;

import com.inopek.beans.UserBean;

public interface UserCustomDao {

	/**
	 * Find user by imei number
	 * @param imieNumber
	 * @return
	 */
	UserBean findByImeiNumber(String imeiNumber);
}
