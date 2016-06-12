package es.ucm.visavet.gbf.app.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class NewsDetect extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "siteCodeName")
	private Feed site;
	@Lob
	private String title;
	@Lob
	private String link;
	private Date datePub;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "alert_detect_id")
	private AlertAbstract alertDetect;
	private boolean history = false;
	private boolean falPositive = false;
	private boolean mark = false;
	private float score;
	private float scoreLucene;
	@Transient
	private List<Location> location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Float getScore() {
		return score;
	}

	public void setScore(float score2) {
		this.score = score2;
	}

	public float getScoreLucene() {
		return scoreLucene;
	}

	public void setScoreLucene(float scoreLucene) {
		this.scoreLucene = scoreLucene;
	}

	public List<Location> getLocation() {
		return location;
	}

	public void setLocation(List<Location> location) {
		this.location = location;
	}
	
	public void addLocation(Location location) {
		if (this.location == null)
			this.location = new ArrayList<Location>();
		this.location.add(location);
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
		if (version != other.getVersion())
			return false;
		return true;
	}

}
