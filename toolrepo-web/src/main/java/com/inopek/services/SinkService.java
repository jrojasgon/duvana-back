package com.inopek.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.inopek.beans.SinkBean;
import com.inopek.beans.UserBean;
import com.inopek.enums.ProfileEnum;


public interface SinkService {
	
	/**
	 * 
	 * @param sinks
	 * @param user
	 * @return a map with file names and a flag to say it has been saved (true)
	 *         or it already exists (false)
	 */
	HashMap<String, Boolean> prepareAndSave(Set<SinkBean> sinks, UserBean user, ProfileEnum profile);
	
	/**
	 * Check if reference exists depending on step if step before check also
	 * imagePathBeforeClean exists otherwise check imageAfter exists
	 * 
	 * @param sinkBean
	 * @return
	 */
	boolean checkReferenceExists(SinkBean sinkBean, boolean stepBefore);
	
	/**
	 * Save beans with creation user
	 * 
	 * @param sinks
	 * @param user
	 * @return
	 */
	SinkBean prepareAndSave(SinkBean sinks, UserBean user);
	
	/**
	 * Update beans with user update
	 * 
	 * @param sinks
	 * @param user
	 * @return
	 */
	SinkBean update(SinkBean sinks, UserBean user);
	
	/**
	 * Find sinks depending on criteria
	 * 
	 * @param startDate
	 * @param endDate
	 * @param clientName
	 * @param reference
	 * @return
	 */
	ArrayList<SinkBean> findAllSinksByDateAnClientAndReference(Date startDate, Date endDate, String clientName, String reference);
	
	/**
	 * Delete bean
	 * 
	 * @param sinkBean
	 * @return
	 */
	boolean deleteSink(SinkBean sinkBean);
	
	/**
	 * Find sinks depending on criteria
	 * 
	 * @param reference
	 * @param clientName
	 * @param stepBefore
	 * @return
	 */
	boolean findByReferenceAndClientAndStep(String reference, String clientName, boolean stepBefore);
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param clientName
	 * @return
	 */
	List<SinkBean> findSinksByDateAndClientAndReferenceForView(Date startDate, Date endDate, String clientName, String reference);
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	List<SinkBean> findSinkBeansByIds(List<Long> ids);
	
}
