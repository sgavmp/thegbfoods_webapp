package es.ucm.visavet.gbf.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.ucm.visavet.gbf.app.domain.Topic;

@Repository
public interface TopicRepository extends CrudRepository<Topic, String> {

}
