package com.ucm.ilsa.veterinaria.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.ScrapStatistics;
import com.ucm.ilsa.veterinaria.domain.Statistics;

public interface ScrapStatisticsRepository extends CrudRepository<ScrapStatistics, Date> {

}
