package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.physics.CircularShape;
import ar.edu.itba.pedestriansim.back.entity.physics.Collitions;

public class SpringForceModel {

	private static final float WALL_MAGIC = 10;
	private final Vector2f cache = new Vector2f();
	
	private final float _K = 10000;
	private final Vector2f closestPointCache = new  Vector2f();
	private final Vector2f nullForce = new Vector2f();

	public Vector2f getForce(CircularShape cshape1, CircularShape cshape2) {
		Circle shape1 = cshape1.getShape();
		Circle shape2 = cshape2.getShape();
		if (Collitions.touching(shape1, shape2)) {
			float overlapping = Collitions.overlapping(shape1, shape2);
			cache.set(cshape1.getCenter());
			Vector2f director = cache.sub(cshape2.getCenter()).normalise();
			return director.scale(_K * overlapping);
		}
		return nullForce;
	}

	public Vector2f getForce(CircularShape shape1, Shape shape) {
		if (shape instanceof Line) {
			return getForce(shape1, (Line) shape);
		}
		if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			Line closest = null;
			float closestDistance = 0;
			Line border = new Line(0, 0);
			for (int i =  0; i < 4; i++) {
				border.set(rectangle.getPoint(i), rectangle.getPoint((i + 1) % 4));
				float currDistance = border.distance(shape1.getCenter());
				if (closest == null || currDistance < closestDistance) {
					closest = border;
					closestDistance = currDistance;
				}
			}
			return getForce(shape1, closest);
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
