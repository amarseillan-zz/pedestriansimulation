package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class SpringForceModel {

	private final float _K;
	private Vector2f circleRectangleDirectorCache = new Vector2f();
	private final Vector2f nullForce = new Vector2f();

	public SpringForceModel(float K) {
		_K = K;
	}

	public Vector2f getForce(CircularShape shape1, CircularShape shape2) {
		if (Collitions.colliding(shape1.getShape(), shape2.getShape())) {
			float overlapping = Collitions.overlapping(shape1.getShape(), shape2.getShape());
			Vector2f director = shape1.getCenter().sub(shape2.getCenter()).normalise();
			return director.scale(_K * overlapping);
		}
		return nullForce;
	}

	public Vector2f getForce(CircularShape shape1, Rectangle rectangle) {
		if (Collitions.colliding(shape1.getShape(), rectangle)) {
			float overlapping = Collitions.overlapping(shape1.getShape(), rectangle, circleRectangleDirectorCache);
			return circleRectangleDirectorCache.scale(_K * overlapping);
		}
		return nullForce;
	}
}
