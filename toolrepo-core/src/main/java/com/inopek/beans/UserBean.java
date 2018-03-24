package com.inopek.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_USER")
public class UserBean implements Serializable {
	
	private static final long	serialVersionUID	= 1118930171242157139L;
	private Long					id;
	private String					imiNumber;
	private Date					creationDate;
	private Date					updateDate;
	
	public UserBean() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USR_ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "USR_IMI_NUMBER", length = 250)
	public String getImiNumber() {
		return imiNumber;
	}
	
	public void setImiNumber(String imiNumber) {
		this.imiNumber = imiNumber;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "USR_CREATION_DATE")
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "USR_UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
}
