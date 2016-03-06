package com.ucm.ilsa.veterinaria.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Statistics;

public interface StatisticsRepository extends CrudRepository<Statistics, Date> {

	@Query(value="SELECT week(s.FECHA) SEMANA, SUM(s.ALERTA), SUM(s.TOTAL),SUM(s.RIESGO) FROM ESTADISTICAS s group by SEMANA",nativeQuery = true)
	List<Object[]> getStatsByWeek();
	@Query(value="SELECT month(s.FECHA) MES, SUM(s.ALERTA), SUM(s.TOTAL),SUM(s.RIESGO) FROM ESTADISTICAS s group by MES",nativeQuery = true)
	List<Object[]> getStatsByMonth();
	@Query(value="SELECT *  FROM alert_score where fecha = :fecha order by score_avg desc limit :limit",nativeQuery = true)
	List<Object[]> getAlertSocreAvgDay(@Param("fecha")String fecha, @Param("limit")Integer limit);
	@Query(value="SELECT *  FROM alert_score where fecha <= :from and fecha > :to order by score_avg desc LIMIT :limit",nativeQuery = true)
	List<Object[]> getAlertSocreAvgBetween(@Param("from")String from,@Param("to")String to, @Param("limit")Integer limit);
	@Query(value="SELECT *  FROM risk_score where fecha = :fecha order by score_avg desc limit :limit",nativeQuery = true)
	List<Object[]> getRiskSocreAvgDay(@Param("fecha")String fecha, @Param("limit")Integer limit);
	@Query(value="SELECT *  FROM risk_score where fecha <= :from and fecha > :to order by score_avg desc LIMIT :limit",nativeQuery = true)
	List<Object[]> getRiskSocreAvgBetween(@Param("from")String from,@Param("to")String to, @Param("limit")Integer limit);
}
