package com.inopek.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.inopek.beans.SinkBean;
import com.inopek.dao.SinkCustomDao;

@Transactional
@Repository
public class SinkDaoImpl extends AbstractDao implements SinkCustomDao {

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<SinkBean> findAllSinksByDateAnClientAndReference(Date startDate, Date endDate, String clientName,
			String reference) {
		String strQuery = "FROM SinkBean WHERE sinkCreationDate BETWEEN :startDate AND :endDate AND client.name = :clientName";
		Query query = buildQueyAndParameterForClientAndReferenceAndDates(startDate, endDate, clientName, reference,
				strQuery);
		return (ArrayList<SinkBean>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SinkBean> findAllSinksByDateAnClientAndReferenceForView(Date startDate, Date endDate, String clientName,
			String reference) {

		String strQuery = "select new com.inopek.beans.SinkBean (id, sinkStatusId,sinkTypeId, length, pipeLineDiameterId, pipeLineLength, plumbOptionId, "
				+ "reference, observations) from SinkBean "
				+ "where sinkCreationDate between :startDate and :endDate and client.name = :clientName";

		Query query = buildQueyAndParameterForClientAndReferenceAndDates(startDate, endDate, clientName, reference,
				strQuery);
		return (List<SinkBean>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SinkBean findByReferenceAndClient(String reference, Long clientId) {
		Query query = getCurrentSession()
				.createQuery("FROM SinkBean WHERE reference = :reference AND client.id = :clientId");
		query.setParameter("reference", reference);
		query.setParameter("clientId", clientId);
		query.setFirstResult(0);
		List<SinkBean> results = (List<SinkBean>) query.list();
		if (CollectionUtils.hasUniqueObject(results)) {
			return results.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SinkBean findByReferenceAndClientAndStep(String reference, String clientName, boolean stepBefore) {
		String strQuery = "FROM SinkBean WHERE reference = :reference AND client.name = :clientName";
		strQuery += stepBefore ? " AND imagePathBeforeClean IS NOT NULL " : " AND imagePathAfterClean IS NOT NULL ";
		Query query = getCurrentSession().createQuery(strQuery);
		query.setParameter("reference", reference);
		query.setParameter("clientName", clientName);
		List<SinkBean> results = (List<SinkBean>) query.list();
		if (CollectionUtils.hasUniqueObject(results)) {
			return results.get(0);
		}
		return null;
	}

	private Query buildQueyAndParameterForClientAndReferenceAndDates(Date startDate, Date endDate, String clientName,
			String reference, String strQuery) {
		if (!StringUtils.isEmpty(reference)) {
			strQuery += " AND reference = :reference";
		}
		Query query = getCurrentSession().createQuery(strQuery);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("clientName", clientName);
		if (!StringUtils.isEmpty(reference)) {
			query.setParameter("reference", reference);
		}
		return query;
	}

}
