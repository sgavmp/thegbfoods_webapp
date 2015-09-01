package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;

@Repository
public interface AlertDetectRepository extends CrudRepository<AlertDetect, Long> {

	public List<AlertDetect> readAllByCheckIsTrueOrderByCreateDateDesc();
	public List<AlertDetect> readAllByCheckIsFalseOrderByCreateDateDesc();
	public AlertDetect findByCheckIsFalseAndAlertOrderByCreateDateDesc(Alert alert);
	public List<AlertDetect> readAllByCheckIsFalseAndNewsDetectDatePubAfterOrderByCreateDateDesc(Date date);
}
