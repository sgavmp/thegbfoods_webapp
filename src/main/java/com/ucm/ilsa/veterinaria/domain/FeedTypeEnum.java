package com.ucm.ilsa.veterinaria.domain;

public enum FeedTypeEnum {
	
	periodico(1), revistaCientifica(21), revista(2), blogNutricional(31), blog(3), institucional(4);
	
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
