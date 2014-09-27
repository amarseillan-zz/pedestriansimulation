package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.RepulsionForceValues;

public class ExponentialRepulsionForce implements RepulsionForce {

	private final Vector2f cache = new Vector2f();

	@Override
	public Vector2f between(Vector2f from, Vector2f point, RepulsionForceValues values) {
		cache.set(from);
		float intensity = intensity(from, point, values);
		cache.sub(point).normalise().scale(intensity);
		return cache;
	}

	private float intensity(Vector2f p1, Vector2f p2, RepulsionForceValues values) {
		float distance = p1.distance(p2);
		return (float) (values.alpha() * Math.exp(-distance / values.beta()));
	}

}
