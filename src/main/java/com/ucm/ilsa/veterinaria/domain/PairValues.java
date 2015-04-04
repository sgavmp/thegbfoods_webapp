package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PairValues {
	@Column(name="attribute")
	private String key;
	@Column(name="value_css")
	private String value;
	
	public PairValues() {
	
	}
	
	public PairValues(String key, String value) {
		this.key=key;
		this.value=value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
