package es.ucm.visavet.gbf.app.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScrapStatistics {

	@Id
	@Column(name = "FECHA")
	private Date date;
	@Column(name = "TOTAL")
	private Integer total = 0;

	public ScrapStatistics() {

	}

	public ScrapStatistics(Date date) {
		this.date = date;
	}

	public ScrapStatistics(Date date, Integer total) {
		this.date = date;
		this.total = total;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}