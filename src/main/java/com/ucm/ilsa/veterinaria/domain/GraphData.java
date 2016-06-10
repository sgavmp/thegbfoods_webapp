package com.ucm.ilsa.veterinaria.domain;

import java.util.List;

import com.google.common.collect.Lists;

public class GraphData {

	public List<Object> labels,noticias,alertas;
	
	public GraphData() {
		this.labels = Lists.newArrayList();
		this.noticias = Lists.newArrayList();
		this.alertas = Lists.newArrayList();
	}
}
