package com.ucm.ilsa.veterinaria.domain;

public enum AlertLevel {
	
	yellow(1),orange(2),red(3);
	
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
