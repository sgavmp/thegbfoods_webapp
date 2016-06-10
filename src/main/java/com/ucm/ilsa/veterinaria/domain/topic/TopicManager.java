package com.ucm.ilsa.veterinaria.domain.topic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.ucm.ilsa.veterinaria.domain.FeedPlaceEnum;
import com.ucm.ilsa.veterinaria.domain.FeedTypeEnum;
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
	public Reader getDefinition(String topic) throws UnsupportedEncodingException {
		Topic item = topicRepository.findOne(topic);
		return new InputStreamReader(new ByteArrayInputStream(item.getWords().getBytes()),"UTF-8");
	}

	@Override
	public boolean existsSourceType(String type) {
		try {
			FeedTypeEnum eType = FeedTypeEnum.valueOf(type);
			return (eType!=null)?true:false;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	@Override
	public boolean existsSourceLocation(String location) {
		try {
			FeedPlaceEnum eLoc = FeedPlaceEnum.valueOf(location);
			return (eLoc!=null)?true:false;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	@Override
	public int getSourceType(String type) {
		return FeedTypeEnum.valueOf(type).getValue();
	}

	@Override
	public int getSourceLocation(String location) {
		return FeedPlaceEnum.valueOf(location).getValue();
	}

	@Override
	public Topic getTopic(String topic) {
		return topicRepository.findOne(topic);
	}

}
