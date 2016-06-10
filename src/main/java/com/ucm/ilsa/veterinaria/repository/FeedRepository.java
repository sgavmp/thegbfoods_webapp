package com.ucm.ilsa.veterinaria.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;

@Repository
public interface FeedRepository extends PagingAndSortingRepository<Feed, Long> {

	public List<Feed> findAllByOrderByNameAsc();
	
}
