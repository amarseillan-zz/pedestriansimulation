package ar.edu.itba.pedestriansim.back.entity;

public class RepulsionForceValues {

	private float _alpha;
	private float _beta;

	public RepulsionForceValues() {
		this(0, 0);
	}

	public RepulsionForceValues(float alpha, float beta) {
		_alpha = alpha;
		_beta = beta;
	}

	public RepulsionForceValues setAlpha(float alpha) {
		_alpha = alpha;
		return this;
	}

	public float alpha() {
		return _alpha;
	}

	public RepulsionForceValues setBeta(float beta) {
		_beta = beta;
		return this;
	}

	public float beta() {
		return _beta;
	}

}
