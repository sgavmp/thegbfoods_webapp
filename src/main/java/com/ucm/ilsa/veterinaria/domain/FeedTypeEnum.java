package com.ucm.ilsa.veterinaria.domain;

public enum FeedTypeEnum {
	
	general(0),periodico(10), revistaCientifica(21), revista(20), blogNutricional(31), blog(30), institucional(40);
	
	private Integer value;

    private FeedTypeEnum(Integer value) {
            this.value = value;
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}


}
