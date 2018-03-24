package com.inopek.services;

import com.inopek.beans.UserBean;

public interface UserService {
	
	/**
	 * Find user by imeiNumber
	 * @param imeiNumber
	 * @return
	 */
	UserBean findByImeiNumber(String imeiNumber);
}
