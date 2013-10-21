package ar.edu.itba.pedestriansim.back.rand;

public class CappedGaussianRandomGenerator extends GaussianRandomGenerator {

	private float _min, _max;
	
	public CappedGaussianRandomGenerator(float mean, float stDev, float min, float max) {
		super(mean, stDev);
		_min = min;
		_max = max;
	}

	@Override
	public float generate() {
		return Math.min(_max, Math.max(_min, super.generate()));
	}
}
