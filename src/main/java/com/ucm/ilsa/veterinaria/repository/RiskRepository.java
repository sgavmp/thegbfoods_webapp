package com.ucm.ilsa.veterinaria.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Risk;

@Repository
public interface RiskRepository extends CrudRepository<Risk, String> {

}
