package es.ucm.visavet.gbf.app.domain;

public enum WebLevel {
	
	yellow(1),orange(2),red(3),blue(4);
	
	private int value;

    private WebLevel(int value) {
            this.value = value;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
