package com.ucm.ilsa.veterinaria.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Statistics;

public interface StatisticsRepository extends CrudRepository<Statistics, Date> {

	@Query(value="SELECT week(s.date) Week, SUM(s.num_alerts), SUM(s.num_news) FROM statistics s group by week(s.date)",nativeQuery = true)
	List<Object[]> getStatsByWeek();
	@Query(value="SELECT month(s.date) Week, SUM(s.num_alerts), SUM(s.num_news) FROM statistics s group by month(s.date)",nativeQuery = true)
	List<Object[]> getStatsByMonth();
}
