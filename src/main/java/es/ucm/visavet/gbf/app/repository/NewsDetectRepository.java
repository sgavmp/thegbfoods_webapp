package es.ucm.visavet.gbf.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.ucm.visavet.gbf.app.domain.AlertAbstract;
import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.NewsDetect;

public interface NewsDetectRepository extends CrudRepository<NewsDetect, Long> {
	
	public NewsDetect findFirstByAlertDetectAndLink(AlertAbstract alertDetect, String link);
	public List<NewsDetect> findAllDistinctBySiteOrderByDatePubDesc(Feed site);

}
