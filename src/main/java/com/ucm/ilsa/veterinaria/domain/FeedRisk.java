package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.validator.group.GroupSequenceProvider;

@Entity
public class FeedRisk extends SiteAbstract {

	public FeedRisk() {
		super();
	}
	
	public FeedRisk(FeedForm feed) {
		super(feed);
	}

}
