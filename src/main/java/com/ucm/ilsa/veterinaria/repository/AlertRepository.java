package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;

public interface AlertRepository extends CrudRepository<Alert, Long> {

	public List<Alert> readAllByCheckIsTrueOrderByDatePubDesc();
	public List<Alert> readAllByCheckIsFalseOrderByDatePubDesc();
	public List<Alert> findAllByCheckIsTrueAndSiteOrderByDatePubDesc(String site);
	public List<Alert> findAllByCheckIsFalseAndSiteOrderByDatePubDesc(String site);
}
