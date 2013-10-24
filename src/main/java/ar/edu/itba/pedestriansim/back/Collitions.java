package ar.edu.itba.pedestriansim.back;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Collitions {
	
	private static final Logger logger = Logger.getLogger(Collitions.class);

	private static final Line rectangleEdge = new Line(0, 0);
	private static final Line radialLine = new Line(0, 0);

	public static float overlapping(Circle circle, Rectangle rectangle, Vector2f directorCache) {
		for (int i = 0; i < 4; i++) {
			rectangleEdge.set(rectangle.getPoint(i), rectangle.getPoint((i + 1) % 4));
			if (rectangleEdge.intersects(circle)) {
				Vector2f end = new Vector2f(circle.getCenter()).add(
					new Vector2f(rectangleEdge.getNormal(0)).scale(circle.getRadius()));
				radialLine.set(circle.getCenter(), new float[] {end.x, end.y});
				directorCache.set(radialLine.getEnd());
				directorCache.sub(radialLine.getStart());
				Vector2f intersection = radialLine.intersect(rectangleEdge);
				return circle.getRadius() - new Vector2f(circle.getCenter()).distance(intersection);
			}
		}
		throw new IllegalStateException("Not overlapping");
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
	
	public static boolean colliding(Shape shape1, Shape shape2) {
		return shape1.intersects(shape2) || shape1.contains(shape2) || shape2.contains(shape1);
	}
}
