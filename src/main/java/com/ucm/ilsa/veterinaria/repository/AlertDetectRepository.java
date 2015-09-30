package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;

@Repository
public interface AlertDetectRepository extends CrudRepository<AlertDetect, Long> {

	public List<AlertDetect> readAllByCheckIsTrueOrderByCreateDateDesc();
	public List<AlertDetect> readAllByCheckIsFalseOrderByCreateDateDesc();
	public AlertDetect findByCheckIsFalseAndAlertOrderByCreateDateDesc(AlertAbstract alert);
	public List<AlertDetect> readAllByCheckIsFalseAndNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public List<AlertDetect> readAllDistinctByNewsDetectSiteAndCheckIsFalse(Feed Site);
}
