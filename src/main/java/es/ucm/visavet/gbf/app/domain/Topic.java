package es.ucm.visavet.gbf.app.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;

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
		if (references==null)
			references = Lists.newArrayList();
		return references;
	}
	public void setReferences(List<Topic> references) {
		this.references = references;
	}

	
}
