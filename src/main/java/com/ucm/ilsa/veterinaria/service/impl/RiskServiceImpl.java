package com.ucm.ilsa.veterinaria.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.repository.RiskRepository;

@Service
public class RiskServiceImpl {
	
	@Autowired
	private RiskRepository repository;
	
	public List<Risk> getAllAlert() {
		return Lists.newArrayList(repository.findAll());
	}
	
	public Risk getOneById(String word) {
		return repository.findOne(word);
	}
	
	public Risk update(Risk word) {
		return repository.save(word);
	}
	
	public Risk create(Risk word) {
		return repository.save(word);
	}
	
	public void remove(Risk word) {
		repository.delete(word);
	}
	
	public void remove(String word) {
		repository.delete(word);
	}

}
