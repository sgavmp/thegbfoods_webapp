package es.ucm.visavet.gbf.app.repository;

import org.springframework.data.repository.CrudRepository;

import es.ucm.visavet.gbf.app.domain.News;

public interface NewsRepository extends CrudRepository<News, Long> {
	

}
