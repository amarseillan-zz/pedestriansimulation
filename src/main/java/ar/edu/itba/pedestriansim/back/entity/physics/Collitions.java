package ar.edu.itba.pedestriansim.back.entity.physics;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Collitions {

	private static final Logger logger = Logger.getLogger(Collitions.class);

	private static final Vector2f _result = new Vector2f();

	public static float overlapping(Circle circle, Line line) {
		Vector2f center = new Vector2f(circle.getCenter());
		line.getClosestPoint(center, _result);
		float distSq = _result.distanceSquared(center);
		if (distSq < circle.radius * circle.radius) {
			float dist = (float) Math.sqrt(distSq);
			return circle.radius - dist;
		}
		return 0;
	}

	public static float overlapping(Circle circle1, Circle circle2) {
		Vector2f center1 = new Vector2f(circle1.getCenter());
		Vector2f center2 = new Vector2f(circle2.getCenter());
		float overlapping = (circle1.getRadius() + circle2.getRadius()) - center1.distance(center2);
		if (overlapping < 0) {
			logger.error("Negative overlapping: " + overlapping + ". Defaulting to 0.");
			overlapping = 0;
		}
		return overlapping;
	}

	public static boolean touching(Shape shape1, Shape shape2) {
		return shape1.intersects(shape2) || shape1.contains(shape2) || shape2.contains(shape1);
	}
}
