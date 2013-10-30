package ar.edu.itba.pedestriansim.back.rand;

import java.util.Random;

public class GaussianRandomGenerator implements RandomGenerator {

    private Random random;
    private float _mean, _stDev;

    public GaussianRandomGenerator(float mean, float stDev) {
        _mean = mean;
        _stDev = stDev;
        random = new Random();
    }

    @Override
    public float generate() {
        return (float) (_stDev * random.nextGaussian() + _mean);
    }

}
