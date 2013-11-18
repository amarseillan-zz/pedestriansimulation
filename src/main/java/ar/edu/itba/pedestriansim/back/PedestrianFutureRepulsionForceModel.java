package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class PedestrianFutureRepulsionForceModel {

	private final Vector2f cache = new Vector2f();
	
	public Vector2f repulsionForce(Vector2f p1, Vector2f other) {
		cache.set(p1);
		cache.sub(other).normalise().scale(intensity(p1, other));
		return cache;
	}

	private float intensity(Vector2f p1, Vector2f p2) {
		float distance = p1.distance(p2);
		return Math.min(40, 10 / (distance * distance));
	}
}
