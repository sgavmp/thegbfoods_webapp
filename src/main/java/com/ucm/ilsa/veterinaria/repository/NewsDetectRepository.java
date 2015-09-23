package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;

public interface NewsDetectRepository extends CrudRepository<NewsDetect, Long> {
	
	public NewsDetect findFirstByAlertDetectAndLink(AlertDetect alertDetect, String link);
	public List<NewsDetect> findAllDistinctBySiteOrderByDatePubDesc(Feed site);

}
