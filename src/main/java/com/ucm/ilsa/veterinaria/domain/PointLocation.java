package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Embeddable;

import com.bericotech.clavin.gazetteer.CountryCode;

@Embeddable
public class PointLocation {
	
	private String name;
	private Double latitude;
	private Double longitude;
	private CountryCode country;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public CountryCode getCountry() {
		return country;
	}
	public void setCountry(CountryCode country) {
		this.country = country;
	}

}
