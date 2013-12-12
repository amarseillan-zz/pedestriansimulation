package ar.edu.itba.pedestriansim.back.replusionforce;

import org.newdawn.slick.geom.Vector2f;

public class FutureRepulsionForce1 implements RepulsionForce {

	private final Vector2f cache = new Vector2f();
	private float _max, _alpha;

	public FutureRepulsionForce1(float max, float alpha) {
		_max = max;
		_alpha = alpha;
	}

	@Override
	public Vector2f apply(Vector2f from, Vector2f other) {
		cache.set(from);
		cache.sub(other).normalise().scale(intensity(from, other));
		return cache;
	}

	private float intensity(Vector2f p1, Vector2f p2) {
		float distance = p1.distance(p2);
		return Math.min(_max, _alpha / (distance * distance));
	}

}
