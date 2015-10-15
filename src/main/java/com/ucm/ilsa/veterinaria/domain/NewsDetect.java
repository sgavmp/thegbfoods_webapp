package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.gazetteer.GeoName;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
public class NewsDetect extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "siteCodeName")
	private SiteAbstract site;
	@Lob
	private String title;
	@Lob
	@Column(name = "link")
	private String link;
	private Date datePub;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> wordsDetect;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "alert_detect_id")
	private List<Location> locationsNear;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "news_detect_locations", joinColumns = @JoinColumn(name = "NEWS_ID") )
	private List<PointLocation> locations;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "alert_detect_id")
	private AlertAbstract alertDetect;
	private boolean history = false;
	private boolean falPositive = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getWordsDetect() {
		return wordsDetect;
	}

	public void setWordsDetect(List<String> wordsDetect) {
		this.wordsDetect = wordsDetect;
	}

	public List<Location> getLocationsNear() {
		return locationsNear;
	}

	public void setLocationsNear(List<Location> locationsNear) {
		this.locationsNear = locationsNear;
	}

	public SiteAbstract getSite() {
		return site;
	}

	public void setSite(SiteAbstract site) {
		this.site = site;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getDatePub() {
		return datePub;
	}

	public void setDatePub(Date datePub) {
		this.datePub = datePub;
	}

	@JsonIgnore
	public AlertAbstract getAlertDetect() {
		return alertDetect;
	}

	public void setAlertDetect(AlertAbstract alertDetect) {
		this.alertDetect = alertDetect;
	}

	public Map<CountryCode, List<Location>> getCountryWithLocations() {
		Map<CountryCode, List<Location>> mapa = new HashMap<CountryCode, List<Location>>();
		if (locationsNear != null) {
			for (Location loc : locationsNear) {
				List<Location> lista = mapa.containsKey(loc.getCountry()) ? mapa.get(loc.getCountry())
						: new ArrayList<Location>();
				lista.add(loc);
				mapa.put(loc.getCountry(), lista);
			}
		}
		return mapa;
	}

	public List<PointLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<PointLocation> locations) {
		this.locations = locations;
	}

	public boolean getHistory() {
		return history;
	}

	public void setHistory(boolean isHistory) {
		this.history = isHistory;
	}

	public boolean getFalPositive() {
		return falPositive;
	}

	public void setFalPositive(boolean isFalsePositive) {
		this.falPositive = isFalsePositive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsDetect other = (NewsDetect) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version!=other.getVersion())
			return false;
		return true;
	}

	
}
