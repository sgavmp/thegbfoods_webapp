package es.ucm.visavet.gbf.app.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import es.ucm.visavet.gbf.app.domain.Feed;

@Repository
public interface FeedRepository extends PagingAndSortingRepository<Feed, Long> {

	public List<Feed> findAllByOrderByNameAsc();
	
}
