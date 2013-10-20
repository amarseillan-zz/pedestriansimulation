package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class SpringForceModel {

	private final static float K = 100;
	private final Vector2f nullForce = new Vector2f();

	public Vector2f getForce(CircularShape shape1, CircularShape shape2) {
		if (shape1.isCollidingWith(shape2)) {
			Vector2f center1 = shape1.getCenter();
			Vector2f center2 = shape2.getCenter();
			float overlapping = center1.distance(shape2.getCenter());
			Vector2f director = new Vector2f(center1).sub(center2).normalise();
			return director.scale(K * overlapping);
		}
		return nullForce;
	}

}
