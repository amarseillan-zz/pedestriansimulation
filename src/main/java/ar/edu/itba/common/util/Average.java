package ar.edu.itba.common.util;

public class Average {

	Float count;
	Long qty;
	
	public Average() {
		this.count = 0f;
		this.qty = 0l;
	}
	
	public void add(Float value) {
		this.count += value;
		this.qty++;
	}
	
	public Float getAverage() {
		if (this.qty == 0) {
			return 0f;
		}
		return this.count / this.qty;
	}
}
