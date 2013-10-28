package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class SpringForceModel {

	private static final float WALL_MAGIC = 10;
	
	private final float _K;
	private final Vector2f closestPointCache = new  Vector2f();
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

	public Vector2f getForce(CircularShape shape1, Shape shape) {
		if (shape instanceof Line) {
			return getForce(shape1, (Line) shape);
		}
		throw new IllegalStateException("Not implemented");
	}

	public Vector2f getForce(CircularShape shape1, Line line) {
		float overlapping = Collitions.overlapping(shape1.getShape(), line);
		if (overlapping > 0) {
			line.getClosestPoint(shape1.getCenter(), closestPointCache);
			return shape1.getCenter().sub(closestPointCache).scale(_K * overlapping * WALL_MAGIC);
		}
		return nullForce;
	}
}
