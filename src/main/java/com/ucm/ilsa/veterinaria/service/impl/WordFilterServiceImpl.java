package com.ucm.ilsa.veterinaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.WordFilter;
import com.ucm.ilsa.veterinaria.repository.WordFilterRepository;

@Service
public class WordFilterServiceImpl {
	
	@Autowired
	private WordFilterRepository repository;
	
	public List<WordFilter> getAllWordFilter() {
		return Lists.newArrayList(repository.findAll());
	}
	
	public WordFilter getOneById(String word) {
		return repository.findOne(word);
	}
	
	public WordFilter update(WordFilter word) {
		return repository.save(word);
	}
	
	public WordFilter create(WordFilter word) {
		return repository.save(word);
	}
	
	public void remove(WordFilter word) {
		repository.delete(word);
	}
	
	public void remove(String word) {
		repository.delete(word);
	}

}
