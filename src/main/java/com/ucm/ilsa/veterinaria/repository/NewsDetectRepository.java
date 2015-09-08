package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;

public interface NewsDetectRepository extends CrudRepository<NewsDetect, Long> {
	
	public NewsDetect findBySiteAndLink(Feed site, String link);

}
