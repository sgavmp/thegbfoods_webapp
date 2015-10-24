package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
public class Dictionary extends BaseEntity {
	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	@NotNull
	protected String title;
	private String titleEn;
	@NotNull
	@Lob
	private String words;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}

	public String getTitleEn() {
		return titleEn;
	}
	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}
	public Dictionary bind(Dictionary location) {
		this.id = location.id;
		this.createDate = location.createDate;
		this.updateDate = location.updateDate;
		this.version = location.version;
		return this;
	}
}
