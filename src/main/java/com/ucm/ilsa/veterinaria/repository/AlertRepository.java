package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;

@Repository
public interface AlertRepository extends CrudRepository<Alert, String> {
	public List<Alert> readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public List<Alert> readAllDistinctByNewsDetectSite(Feed Site);
	public List<Alert> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public List<Alert> readAllDistinctByNewsDetectHistoryTrue();
	public List<Alert> readAllDistinctByNewsDetectFalPositiveTrue();
}
