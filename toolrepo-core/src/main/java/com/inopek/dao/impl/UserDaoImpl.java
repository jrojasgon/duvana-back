package com.inopek.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.inopek.beans.UserBean;
import com.inopek.dao.UserCustomDao;


@Transactional
@Repository
public class UserDaoImpl extends AbstractDao implements UserCustomDao {

	public UserBean findByImeiNumber(String imiNumber) {
		Criteria criteria = getCurrentSession().createCriteria(UserBean.class);
		criteria = criteria.add(Restrictions.eq("imiNumber", imiNumber));
		return (UserBean) criteria.uniqueResult();
	}

}
