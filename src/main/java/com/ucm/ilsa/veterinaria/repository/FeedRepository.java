package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Feed;

@Repository
public interface FeedRepository extends CrudRepository<Feed, Long> {

}
