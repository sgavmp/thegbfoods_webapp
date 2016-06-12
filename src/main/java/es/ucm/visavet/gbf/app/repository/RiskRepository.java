package es.ucm.visavet.gbf.app.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.Risk;

@Repository
public interface RiskRepository extends PagingAndSortingRepository<Risk, Long> {
	public Set<Risk> readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public Set<Risk> readAllDistinctByNewsDetectSite(Feed Site);
	public Set<Risk> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public Set<Risk> readAllDistinctByNewsDetectHistoryTrue();
	public Set<Risk> readAllDistinctByNewsDetectFalPositiveTrue();
	public Set<Risk> findAllByOrderByTitleAsc();
}
