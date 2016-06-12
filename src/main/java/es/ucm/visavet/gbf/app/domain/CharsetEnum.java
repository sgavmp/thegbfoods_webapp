package es.ucm.visavet.gbf.app.domain;

public enum CharsetEnum {
	
	ASCII("US-ASCII"),ISO("ISO-8859-1"),UTF8("UTF-8"),UTF16BE("UTF-16BE"),UTF16LE("UTF-16LE"),UTF16("UTF-16");
	
	private String value;

    private CharsetEnum(String value) {
            this.value = value;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
