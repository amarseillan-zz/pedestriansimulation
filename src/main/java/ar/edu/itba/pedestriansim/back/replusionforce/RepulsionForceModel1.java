package ar.edu.itba.pedestriansim.back.replusionforce;

import org.newdawn.slick.geom.Vector2f;

public class RepulsionForceModel1 implements RepulsionForce {

	private final Vector2f cache = new Vector2f();
	private float _alpha, _beta;

	public RepulsionForceModel1(float alpha, float beta) {
		_alpha = alpha;
		_beta = beta;
	}

	@Override
	public Vector2f apply(Vector2f from, Vector2f other) {
		cache.set(from);
		float intensity = intensity(from, other);
		cache.sub(other).normalise().scale(intensity);
		return cache;
	}

	private float intensity(Vector2f p1, Vector2f p2) {
		return 500 / p1.distance(p2);
//		float distance = p1.distance(p2);
//		return (float) (_alpha * Math.exp(-distance / _beta));
	}

}
