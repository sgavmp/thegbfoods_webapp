package es.ucm.visavet.gbf.app.domain;

public enum FeedPlaceEnum {
	
	general(0),españa(1), italia(2), rusia(3), holanda(4), alemania(5), inglaterra(6), portugal(7), francia(8), estadosunidos(9), india(10), marruecos(11);
	
	private Integer value;

    private FeedPlaceEnum(Integer value) {
            this.value = value;
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
