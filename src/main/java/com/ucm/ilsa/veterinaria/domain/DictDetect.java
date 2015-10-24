package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class DictDetect {

	@ManyToOne
	@JoinColumn(name = "dictionary")
	public Dictionary dictionary;
	public String words;
	
	public DictDetect(Dictionary left, String right) {
		this.dictionary = left;
		this.words = right;
	}
	
	public DictDetect() {
		
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}
	
}
