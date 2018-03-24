package com.inopek.bean.view;

import java.util.Date;

import com.inopek.enums.SinkDiameterEnum;
import com.inopek.enums.SinkPlumbOptionEnum;
import com.inopek.enums.SinkStatusEnum;
import com.inopek.enums.SinkTypeEnum;


public class SinkBeanView {

	private Long id;
	private String reference;
	private String lenght;
	private String pipeLenght;
	private String client;
	private String address;
	private String observation;
	private Date sinkCreationDate;
	private SinkTypeEnum type;
	private SinkStatusEnum status;
	private SinkDiameterEnum diameter;
	private SinkPlumbOptionEnum plumbOption;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getLenght() {
		return lenght;
	}

	public void setLenght(String lenght) {
		this.lenght = lenght;
	}

	public String getPipeLenght() {
		return pipeLenght;
	}

	public void setPipeLenght(String pipeLenght) {
		this.pipeLenght = pipeLenght;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Date getSinkCreationDate() {
		return sinkCreationDate;
	}

	public void setSinkCreationDate(Date sinkCreationDate) {
		this.sinkCreationDate = sinkCreationDate;
	}

	public SinkTypeEnum getType() {
		return type;
	}

	public void setType(SinkTypeEnum type) {
		this.type = type;
	}

	public SinkStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SinkStatusEnum status) {
		this.status = status;
	}

	public SinkDiameterEnum getDiameter() {
		return diameter;
	}

	public void setDiameter(SinkDiameterEnum diameter) {
		this.diameter = diameter;
	}

	public SinkPlumbOptionEnum getPlumbOption() {
		return plumbOption;
	}

	public void setPlumbOption(SinkPlumbOptionEnum plumbOption) {
		this.plumbOption = plumbOption;
	}
	
}
