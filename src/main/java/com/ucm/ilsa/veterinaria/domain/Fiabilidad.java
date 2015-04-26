package com.ucm.ilsa.veterinaria.domain;

public enum Fiabilidad {
	Baja(0),Media(1),Alta(2);
	
	private int value;

    private Fiabilidad(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
