package es.ucm.visavet.gbf.app.domain;

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
