package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;

public interface NewsDetectRepository extends CrudRepository<NewsDetect, Long> {
	
	public NewsDetect findFirstByAlertDetectAndLink(AlertAbstract alertDetect, String link);
	public List<NewsDetect> findAllDistinctBySiteOrderByDatePubDesc(Feed site);

}
