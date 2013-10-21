package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class SpringForceModel {

	private final static float K = 1000;
	private final Vector2f nullForce = new Vector2f();

	public Vector2f getForce(CircularShape shape1, CircularShape shape2) {
		if (shape1.isCollidingWith(shape2)) {
			float overlapping = Collitions.overlapping(shape1.getShape(), shape2.getShape());
			Vector2f director = shape1.getCenter().sub(shape2.getCenter()).normalise();
			return director.scale(K * overlapping);
		}
		return nullForce;
	}

}
