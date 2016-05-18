package com.ucm.ilsa.veterinaria.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Statistics;

public interface StatisticsRepository extends CrudRepository<Statistics, Date> {

	@Query(value="SELECT SUM(s.NOTICIAS) NOTICIAS,SUM(s.TOTAL) TOTAL,SUM(s.RIESGO) RIESGO,SUM(s.ALERTA) ALERTA,year(s.FECHA) YEAR,weekofyear(s.FECHA) SEMANA FROM estadisticas s group by SEMANA ORDER BY YEAR desc, SEMANA desc;",nativeQuery=true)
	List<Object[]> getStatsByWeek();
	@Query(value="SELECT SUM(s.NOTICIAS) NOTICIAS,SUM(s.TOTAL) TOTAL,SUM(s.RIESGO) RIESGO,SUM(s.ALERTA) ALERTA, year(s.FECHA) YEAR, month(s.FECHA) MES FROM estadisticas s group by MES ORDER BY YEAR desc, MES desc;",nativeQuery = true)
	List<Object[]> getStatsByMonth();
	@Query(value="SELECT * FROM ESTADISTICAS order by FECHA desc limit 7",nativeQuery = true)
	List<Statistics> getStatsLastWeek();
	@Query(value="SELECT * FROM ESTADISTICAS order by FECHA",nativeQuery = true)
	List<Statistics> getAllStats();
	@Query(value="SELECT *  FROM alert_score where fecha = :fecha order by score_avg desc limit :limit",nativeQuery = true)
	List<Object[]> getAlertSocreAvgDay(@Param("fecha")String fecha, @Param("limit")Integer limit);
	@Query(value="SELECT alert_detect_id,title,fecha,sum(num) num, avg(score_avg) score_avg  FROM alert_score where fecha <= :from and fecha > :to group by title order by score_avg desc limit :limit",nativeQuery = true)
	List<Object[]> getAlertSocreAvgBetween(@Param("from")String from,@Param("to")String to, @Param("limit")Integer limit);
	@Query(value="SELECT *  FROM risk_score where fecha = :fecha order by score_avg desc limit :limit",nativeQuery = true)
	List<Object[]> getRiskSocreAvgDay(@Param("fecha")String fecha, @Param("limit")Integer limit);
	@Query(value="SELECT alert_detect_id,title,fecha,sum(num) num, avg(score_avg) score_avg  FROM risk_score where fecha <= :from and fecha > :to group by title order by score_avg desc limit :limit",nativeQuery = true)
	List<Object[]> getRiskSocreAvgBetween(@Param("from")String from,@Param("to")String to, @Param("limit")Integer limit);
	@Query(value="select name, avg(score) as score from news_locations left join location on(location_id = location.id) where date_pub >= date_sub(now(),INTERVAL 1 WEEK) group by location_id order by score desc LIMIT 7;",nativeQuery = true)
	List<Object[]> getStatsOfLocationLastWeek();
}