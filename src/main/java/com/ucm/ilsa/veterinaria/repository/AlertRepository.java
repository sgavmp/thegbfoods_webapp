package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Risk;

@Repository
public interface AlertRepository extends CrudRepository<Alert, Long> {
	public Set<Alert> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalseAndNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public Set<Alert> readAllDistinctByNewsDetectSite(Feed Site);
	public Set<Alert> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public Set<Alert> readAllDistinctByNewsDetectHistoryTrue();
	public Set<Alert> readAllDistinctByNewsDetectFalPositiveTrue();
	public Set<Alert> findAllByOrderByTitleAsc();
}
