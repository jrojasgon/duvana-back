package com.inopek.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ADDRESS")
public class AddressBean implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	private Long					id;
	private String					street;
	private String					complementStreet;
	private String					zipCode;
	private String					city;
	private String					country;
	private String					neighborhood;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ADR_ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ADR_STREET", length = 250)
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	@Column(name = "ADR_COMPLEMENT_STREET", length = 250)
	public String getComplementStreet() {
		return complementStreet;
	}
	
	public void setComplementStreet(String complementStreet) {
		this.complementStreet = complementStreet;
	}
	
	@Column(name = "ADR_ZIP_CODE", length = 50)
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@Column(name = "ADR_CITY", length = 100)
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name = "ADR_COUNTRY", length = 100)
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(name = "ADR_NEIGHBORHOOD", length = 250)
	public String getNeighborhood() {
		return neighborhood;
	}
	
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

}
