package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Alert extends BaseEntity {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@NotNull
	private String title;
	@NotNull
	@Lob
	private String words;
	@Enumerated(EnumType.ORDINAL)
	private AlertLevel type;

	public Alert() {
		this.words = "";
	}

	public Alert(String word) {
		this.words = word;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public AlertLevel getType() {
		return type;
	}

	public void setType(AlertLevel type) {
		this.type = type;
	}
	

}
