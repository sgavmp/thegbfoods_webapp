package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Location;

public interface PlaceAlertRepository extends CrudRepository<Location, Integer> {

}
