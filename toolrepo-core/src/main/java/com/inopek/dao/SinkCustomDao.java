package com.inopek.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inopek.beans.SinkBean;


public interface SinkCustomDao {

	/**
	 * Find Sink by reference and client id
	 * @param reference
	 * @param clientId
	 * @return
	 */
	SinkBean findByReferenceAndClient(String reference, Long clientId);
	
	/**
	 * search if pair reference-client exists
	 * if step before check also imagePathBeforeClean exists
	 * else check image after exists
	 * @param reference
	 * @param clientId
	 * @return
	 */
	SinkBean findByReferenceAndClientAndStep(String reference, String clientName, boolean stepBefore);
	

	/**
	 * Find beans by criteria
	 * @param startDate
	 * @param endDate
	 * @param clientName
	 * @param reference
	 * @return
	 */
	ArrayList<SinkBean> findAllSinksByDateAnClientAndReference(Date startDate, Date endDate, String clientName, String reference);
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param clientName
	 * @param reference
	 * @return
	 */
	List<SinkBean> findAllSinksByDateAnClientAndReferenceForView(Date startDate, Date endDate, String clientName, String reference);
	
}
 	