package es.ucm.visavet.gbf.app.repository;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import es.ucm.visavet.gbf.app.domain.ScrapStatistics;

public interface ScrapStatisticsRepository extends CrudRepository<ScrapStatistics, Date> {

}
