package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Risk;

@Repository
public interface AlertRepository extends CrudRepository<Alert, Long> {
	public List<Alert> readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public List<Alert> readAllDistinctByNewsDetectSite(Feed Site);
	public List<Alert> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public List<Alert> readAllDistinctByNewsDetectHistoryTrue();
	public List<Alert> readAllDistinctByNewsDetectFalPositiveTrue();
	public List<Alert> findAllByOrderByTitleAsc();
}
