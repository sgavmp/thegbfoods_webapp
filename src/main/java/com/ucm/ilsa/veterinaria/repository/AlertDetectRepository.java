package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;

public interface AlertDetectRepository extends CrudRepository<AlertDetect, Long> {

	public List<AlertDetect> readAllByCheckIsTrueOrderByCreateDateDesc();
	public List<AlertDetect> readAllByCheckIsFalseOrderByCreateDateDesc();
	public AlertDetect findByCheckIsFalseAndAlertOrderByCreateDateDesc(Alert alert);
}
