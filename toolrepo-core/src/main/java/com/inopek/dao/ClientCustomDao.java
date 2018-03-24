package com.inopek.dao;

import com.inopek.beans.ClientBean;

public interface ClientCustomDao {
	
	/**
	 * Find client by name
	 * 
	 * @param clientName
	 * @return
	 */
	ClientBean findByName(String clientName);
	
}
