package com.ucm.ilsa.veterinaria.domain.topic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.ucm.ilsa.veterinaria.domain.Topic;
import com.ucm.ilsa.veterinaria.repository.TopicRepository;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;

@Service
public class TopicManager implements ITopicsManager {

	@Autowired
	private TopicRepository topicRepository;
	
	@Override
	public boolean existsTopic(String topic) {
		return topicRepository.exists(topic);
	}

	@Override
	public Set<String> getDependencies(String topic) {
		Topic item = topicRepository.findOne(topic);
		Set<String> dependencias = Sets.newHashSet();
		if (item!=null) {
			for(Topic aux : item.getReferences())
				dependencias.add(aux.getTitle());
		}
		return dependencias;
	}

	@Override
	public void addDependency(String topic, String ofTopic) {
		// TODO Auto-generated method stub
	}

	@Override
	public InputStream getDefinition(String topic) {
		Topic item = topicRepository.findOne(topic);
		return new ByteArrayInputStream(item.getWords().getBytes());
	}

}
