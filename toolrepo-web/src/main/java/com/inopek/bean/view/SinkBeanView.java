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
	private SinkTypeEnum typeEnum;
	private SinkStatusEnum statusEnum;
	private SinkDiameterEnum diameterEnum;
	private SinkPlumbOptionEnum plumbOptionEnum;

	
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

	public String getType() {
		return this.getTypeEnum() == null ? "" : this.getTypeEnum().getLabel();
	}

	public String getStatus() {
		return this.getStatusEnum() == null ? "" : this.getStatusEnum().getLabel();
	}

	public String getDiameter() {
		return this.getDiameterEnum() == null ? "" : this.getDiameterEnum().getLabel();
	}

	public String getPlumbOption() {
		return this.getPlumbOptionEnum() == null ? "" : this.getPlumbOptionEnum().getLabel();
	}

	public SinkTypeEnum getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(SinkTypeEnum typeEnum) {
		this.typeEnum = typeEnum;
	}
	

	public SinkStatusEnum getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(SinkStatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}

	public SinkDiameterEnum getDiameterEnum() {
		return diameterEnum;
	}

	public void setDiameterEnum(SinkDiameterEnum diameterEnum) {
		this.diameterEnum = diameterEnum;
	}

	public SinkPlumbOptionEnum getPlumbOptionEnum() {
		return plumbOptionEnum;
	}

	public void setPlumbOptionEnum(SinkPlumbOptionEnum plumbOptionEnum) {
		this.plumbOptionEnum = plumbOptionEnum;
	}
	
}
