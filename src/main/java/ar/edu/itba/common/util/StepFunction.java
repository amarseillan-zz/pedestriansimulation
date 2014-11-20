package ar.edu.itba.common.util;

import com.google.common.base.Function;

public class StepFunction implements Function<Float, Float> {

	private final float _x;

	public StepFunction(float x) {
		_x = x;
	}

	@Override
	public Float apply(Float input) {
		return input < _x ? 0f : 1f;
	}

}
