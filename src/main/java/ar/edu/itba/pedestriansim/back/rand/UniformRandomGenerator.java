package ar.edu.itba.pedestriansim.back.rand;

public class UniformRandomGenerator implements RandomGenerator {

    private float _min, _max;

    public UniformRandomGenerator(float min, float max) {
    	if (_min < _max) {
    		throw new IllegalArgumentException("Min MUST be less than the max");
    	}
        _min = min;
        _max = max;
    }

    @Override
    public float generate() {
        return (float) (Math.random() * (_max - _min) + _min);
    }
}
