package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private Feed site;
	@Lob
	private String title;
	@Lob
	@Column(name = "link")
	private String link;
	private Date datePub;
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> wordsDetect;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "alert_detect_id")
	private Set<Location> locationsNear;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "news_detect_locations", joinColumns = @JoinColumn(name = "NEWS_ID") )
	private Set<PointLocation> locations;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "alert_detect_id")
	private AlertAbstract alertDetect;
	private boolean history = false;
	private boolean falPositive = false;
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<DictDetect> dictionaryDetect;
	private boolean mark = false;
	private Double score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<String> getWordsDetect() {
		return wordsDetect;
	}

	public void setWordsDetect(Set<String> wordsDetect) {
		this.wordsDetect = wordsDetect;
	}

	public Set<Location> getLocationsNear() {
		return locationsNear;
	}

	public void setLocationsNear(Set<Location> locationsNear) {
		this.locationsNear = locationsNear;
	}

	public Feed getSite() {
		return site;
	}

	public void setSite(Feed site) {
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

	public Set<PointLocation> getLocations() {
		return locations;
	}

	public void setLocations(Set<PointLocation> locations) {
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

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Set<DictDetect> getDictionaryDetect() {
		return dictionaryDetect;
	}

	public void setDictionaryDetect(Set<DictDetect> dictionaryDetect) {
		this.dictionaryDetect = dictionaryDetect;
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
