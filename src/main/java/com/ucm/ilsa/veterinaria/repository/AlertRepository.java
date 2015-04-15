package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;

public interface AlertRepository extends CrudRepository<Alert, Long> {

	public List<Alert> readAllByCheckIsTrueOrderByDatePubDesc();
	public List<Alert> readAllByCheckIsFalseOrderByDatePubDesc();
	public List<Alert> findAllByCheckIsTrueAndSiteOrderByDatePubDesc(Feed site);
	public List<Alert> findAllByCheckIsFalseAndSiteOrderByDatePubDesc(Feed site);
}
