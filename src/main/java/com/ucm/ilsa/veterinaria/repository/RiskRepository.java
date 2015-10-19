package com.ucm.ilsa.veterinaria.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;

@Repository
public interface RiskRepository extends CrudRepository<Risk, Long> {
	public Set<Risk> readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public Set<Risk> readAllDistinctByNewsDetectSite(Feed Site);
	public Set<Risk> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public Set<Risk> readAllDistinctByNewsDetectHistoryTrue();
	public Set<Risk> readAllDistinctByNewsDetectFalPositiveTrue();
	public Set<Risk> findAllByOrderByTitleAsc();
}
