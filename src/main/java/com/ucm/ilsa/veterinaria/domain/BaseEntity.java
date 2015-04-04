package com.ucm.ilsa.veterinaria.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {

	@Version
	private int version;
	
	@Column(name="createDate", insertable=true, updatable=false)
	private Timestamp createDate;
	@Column(name="updateDate", insertable=false, updatable=true)
	private Timestamp updateDate;
	
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@PrePersist
	void onCreate() {
		this.setCreateDate(new Timestamp((new Date()).getTime()));
	}

	@PreUpdate
	void onPersist() {
		this.setUpdateDate(new Timestamp((new Date()).getTime()));
	}
}
