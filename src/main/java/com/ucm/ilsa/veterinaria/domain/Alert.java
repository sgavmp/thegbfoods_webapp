package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Alert extends AlertAbstract {
	@Enumerated(EnumType.ORDINAL)
	private AlertLevel type;
	
	public AlertLevel getType() {
		return type;
	}

	public void setType(AlertLevel type) {
		this.type = type;
	}

}
