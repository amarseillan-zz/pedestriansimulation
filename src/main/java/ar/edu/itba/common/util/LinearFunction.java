package ar.edu.itba.common.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

public class LinearFunction implements Function<Float, Float> {

	private final float _x0, _x1;
	private final float _d;

	public LinearFunction(float x0, float x1) {
		_x0 = x0;
		_x1 = x1;
		_d = _x1 - x0;
		Preconditions.checkArgument(_x0 < _x1);
	}

	@Override
	public Float apply(Float input) {
		if (input <= _x0) {
			return 0f;
		}
		if (input >= _x1) {
			return 1f;
		}
		return (input - _x0) / _d;
	}

}
