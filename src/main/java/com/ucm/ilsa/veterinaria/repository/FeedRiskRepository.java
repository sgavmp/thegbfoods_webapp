package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;

@Repository
public interface FeedRiskRepository extends CrudRepository<FeedRisk, Long> {

	public List<FeedRisk> findAllByOrderByNameAsc();
	
}
