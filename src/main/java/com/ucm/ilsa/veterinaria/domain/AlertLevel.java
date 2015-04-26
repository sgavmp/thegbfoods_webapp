package com.ucm.ilsa.veterinaria.domain;

public enum AlertLevel {
	
	yellow(0),orange(1),red(2);
	
	private int value;

    private AlertLevel(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
