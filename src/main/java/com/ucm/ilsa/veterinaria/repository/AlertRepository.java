package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;

public interface AlertRepository extends CrudRepository<Alert, String> {

}
