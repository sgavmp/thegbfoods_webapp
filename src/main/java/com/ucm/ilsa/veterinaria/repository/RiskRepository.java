package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Risk;

@Repository
public interface RiskRepository extends CrudRepository<Risk, String> {
	public List<Risk> readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public List<Risk> readAllDistinctByNewsDetectSite(Feed Site);
	public List<Risk> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public List<Risk> readAllDistinctByNewsDetectHistoryTrue();
	public List<Risk> readAllDistinctByNewsDetectFalPositiveTrue();
}
