package com.ucm.ilsa.veterinaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;

@Service
public class AlertServiceImpl {
	
	@Autowired
	private AlertRepository repository;
	
	public List<Alert> getAllAlert() {
		return Lists.newArrayList(repository.findAll());
	}
	
	public Alert getOneById(String word) {
		return repository.findOne(word);
	}
	
	public Alert update(Alert word) {
		return repository.save(word);
	}
	
	public Alert create(Alert word) {
		return repository.save(word);
	}
	
	public void remove(Alert word) {
		repository.delete(word);
	}
	
	public void remove(String word) {
		repository.delete(word);
	}

}
