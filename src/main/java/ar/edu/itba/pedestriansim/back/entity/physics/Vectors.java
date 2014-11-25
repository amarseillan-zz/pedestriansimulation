package ar.edu.itba.pedestriansim.back.entity.physics;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import com.google.common.base.Preconditions;

/**
 * Class used to make calculations with vectors
 * 
 * this is a NON thread safe class!
 */
public class Vectors {

	private static final Vector2f dir = new Vector2f();
	private static final Vector2f lineClosesPoint = new Vector2f();
	private static final Vector2f alignmentVector = new Vector2f();

	private static final Vector2f nullVector = new Vector2f();

	public static Vector2f zero() {
		// if (nullVector.length() != 0) {
		// throw new IllegalStateException(nullVector.toString());
		// }
		return nullVector;
	}

	public static boolean isLeft(Vector2f start, Vector2f end, Vector2f pt) {
		return ((end.x - start.x) * (pt.y - start.y) - (end.y - start.y) * (pt.x - start.x)) > 0;
	}

	public static Vector2f pointBetween(Vector2f start, Vector2f end, float distanceFromStart, Vector2f result) {
		dir.set(end);
		dir.sub(start).normalise();
		result.set(start);
		return result.add(dir.scale(distanceFromStart));
	}

	public static Vector2f alignementVector(Line line, Vector2f pt) {
		line.getClosestPoint(pt, lineClosesPoint);
		alignmentVector.set(lineClosesPoint);
		alignmentVector.sub(pt);
		return alignmentVector;
	}

	public static float angle(Vector2f v1, Vector2f v2) {
		double angle = Math.abs(v1.getTheta() - v2.getTheta());
		if (angle > 180) {
			angle = 360 - angle;
		}
		Preconditions.checkArgument(0 <= angle && angle <= 180);
		return (float) angle;
	}
}
