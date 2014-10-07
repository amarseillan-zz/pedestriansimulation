package ar.edu.itba.common.util;

public class Average {

	private float count;
	private long qty;

	public Average() {
		this.count = 0f;
		this.qty = 0l;
	}

	public void add(float value) {
		this.count += value;
		this.qty++;
	}

	public float getAverage() {
		if (this.qty == 0) {
			return 0f;
		}
		return this.count / this.qty;
	}
}
