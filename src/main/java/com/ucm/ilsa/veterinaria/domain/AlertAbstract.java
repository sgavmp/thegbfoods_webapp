package com.ucm.ilsa.veterinaria.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AlertAbstract extends BaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	@NotNull
	private String title;
	@NotNull
	@Lob
	private String words;
	@OneToMany(mappedBy = "alertDetect", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	protected List<NewsDetect> newsDetect;

	public AlertAbstract() {
		this.words = "";
	}

	public AlertAbstract(String word) {
		this.words = word;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String word) {
		this.words = word;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<NewsDetect> getNewsDetect() {
		return newsDetect;
	}

	public void setNewsDetect(List<NewsDetect> newsDetect) {
		this.newsDetect = newsDetect;
	}
	
}
