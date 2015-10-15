package com.ucm.ilsa.veterinaria.domain;

public enum Language {
	
	SPANISH(0) ,ENGLISH(1);

	private int value;

    private Language(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
