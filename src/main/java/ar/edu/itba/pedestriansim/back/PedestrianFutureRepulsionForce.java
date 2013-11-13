package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class PedestrianFutureRepulsionForce {

	private final Vector2f cache = new Vector2f();
	
	public Vector2f getForce(Vector2f p1, Vector2f other) {
		cache.set(p1);
		cache.sub(other).normalise().scale(intensity(p1, other));
		return cache;
	}

	private float intensity(Vector2f p1, Vector2f p2) {
		float distance = p1.distance(p2);
		return 50 / (distance * distance);
	}
}
