package com.ucm.ilsa.veterinaria.repository;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Statistics;

public interface StatisticsRepository extends CrudRepository<Statistics, Date> {

}
