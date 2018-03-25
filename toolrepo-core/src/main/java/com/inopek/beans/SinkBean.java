package com.inopek.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inopek.deserializer.ImagePathAfterDeserializer;
import com.inopek.deserializer.ImagePathBeforeDeserializer;
import com.inopek.serializer.ImagePathSerializer;

@Entity
@Table(name = "T_SINK")
public class SinkBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long sinkStatusId;
	private Long sinkTypeId;
	private Long length;
	private Long pipeLineDiameterId;
	private Long pipeLineLength;
	private Long plumbOptionId;
	private String reference;
	private String observations;
	private String fileName;
	private AddressBean address;
	private ClientBean client;
	private UserBean userCreation;
	private UserBean userUpdate;
	private Date sinkCreationDate;
	private Date sinkUpdateDate;
	
	@JsonDeserialize(using = ImagePathBeforeDeserializer.class)
	@JsonSerialize(using = ImagePathSerializer.class)
	private String imagePathBeforeClean;
	
	@JsonDeserialize(using = ImagePathAfterDeserializer.class)
	@JsonSerialize(using = ImagePathSerializer.class)
	private String imagePathAfterClean;


	public SinkBean(Long id, Long sinkStatusId, Long sinkTypeId, Long length, Long pipeLineDiameterId,
			Long pipeLineLength, Long plumbOptionId, String reference, String observations) {
		this.id = id;
		this.sinkStatusId = sinkStatusId;
		this.sinkTypeId = sinkTypeId;
		this.length = length;
		this.pipeLineDiameterId = pipeLineDiameterId;
		this.pipeLineLength = pipeLineLength;
		this.plumbOptionId = plumbOptionId;
		this.reference = reference;
		this.observations = observations;
	}

	public SinkBean(Long id, String reference) {
		this.id = id;
		this.reference = reference;
	}

	public SinkBean(String reference) {
		this.reference = reference;
	}
	
	public SinkBean() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SNK_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SNK_REFERENCE", length = 150)
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SNK_ADDRESS_ID", nullable = false)
	public AddressBean getAddress() {
		return address;
	}

	public void setAddress(AddressBean address) {
		this.address = address;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SNK_CLIENT_ID", nullable = false)
	public ClientBean getClient() {
		return client;
	}

	public void setClient(ClientBean client) {
		this.client = client;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SNK_USER_CRE_ID", nullable = false)
	public UserBean getUserCreation() {
		return userCreation;
	}

	public void setUserCreation(UserBean userCreation) {
		this.userCreation = userCreation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SNK_USER_UPD_ID", nullable = false)
	public UserBean getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(UserBean userUpdate) {
		this.userUpdate = userUpdate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SNK_CREATION_DATE")
	public Date getSinkCreationDate() {
		return sinkCreationDate;
	}

	public void setSinkCreationDate(Date sinkCreationDate) {
		this.sinkCreationDate = sinkCreationDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SNK_UPDATE_DATE")
	public Date getSinkUpdateDate() {
		return sinkUpdateDate;
	}

	public void setSinkUpdateDate(Date sinkUpdateDate) {
		this.sinkUpdateDate = sinkUpdateDate;
	}

	@Column(name = "SNK_STATUS_ID")
	public Long getSinkStatusId() {
		return sinkStatusId;
	}

	public void setSinkStatusId(Long sinkStatusId) {
		this.sinkStatusId = sinkStatusId;
	}

	@Column(name = "SNK_TYPE_ID")
	public Long getSinkTypeId() {
		return sinkTypeId;
	}

	public void setSinkTypeId(Long sinkTypeId) {
		this.sinkTypeId = sinkTypeId;
	}

	@Column(name = "SNK_LENGTH")
	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	@Column(name = "SNK_PIPELINE_LENGTH")
	public Long getPipeLineLength() {
		return pipeLineLength;
	}

	public void setPipeLineLength(Long pipeLineLength) {
		this.pipeLineLength = pipeLineLength;
	}

	@Column(name = "SNK_OBSERVATIONS", length = 500)
	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	@Column(name = "SNK_FILE_NAME", length = 500)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "SNK_PIPELINE_DIAMETER_ID")
	public Long getPipeLineDiameterId() {
		return pipeLineDiameterId;
	}

	public void setPipeLineDiameterId(Long pipeLineDiameterId) {
		this.pipeLineDiameterId = pipeLineDiameterId;
	}

	@Column(name = "SNK_PLUMB_OPTION_ID")
	public Long getPlumbOptionId() {
		return plumbOptionId;
	}

	public void setPlumbOptionId(Long plumbOptionId) {
		this.plumbOptionId = plumbOptionId;
	}
	
	@Column(name = "SNK_IMAGE_BEFORE_PATH")
	public String getImagePathBeforeClean() {
		return imagePathBeforeClean;
	}

	public void setImagePathBeforeClean(String imagePathBeforeClean) {
		this.imagePathBeforeClean = imagePathBeforeClean;
	}
	
	@Column(name = "SNK_IMAGE_AFTER_PATH")
	public String getImagePathAfterClean() {
		return imagePathAfterClean;
	}

	public void setImagePathAfterClean(String imagePathAfterClean) {
		this.imagePathAfterClean = imagePathAfterClean;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SinkBean sinkBean = (SinkBean) o;

		return reference.equals(sinkBean.reference);

	}

	@Override
	public int hashCode() {
		return reference.hashCode();
	}

}
