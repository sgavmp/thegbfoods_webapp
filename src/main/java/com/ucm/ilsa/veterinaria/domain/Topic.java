package com.ucm.ilsa.veterinaria.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Topic extends BaseEntity {
	@Id
	private String title;
	@NotNull
	@Lob
	private String words;
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Topic> references;
	

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
	public List<Topic> getReferences() {
		return references;
	}
	public void setReferences(List<Topic> references) {
		this.references = references;
	}

	
}
