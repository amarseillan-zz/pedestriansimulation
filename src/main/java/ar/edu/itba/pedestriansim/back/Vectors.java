package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
/**
 * Class used to make calculations with vectors
 * 
 * this is a NON thread safe class!
 */
public class Vectors {

	private final Vector2f dir = new Vector2f();
	private final Vector2f lineClosesPoint = new Vector2f();
	private final Vector2f alignmentVector = new Vector2f();

	private static final Vector2f nullVector = new Vector2f();

	public static Vector2f nullVector() {
//		if (nullVector.length() != 0) {
//			throw new IllegalStateException(nullVector.toString());
//		}
		return nullVector;
	}

	public Vector2f pointBetween(Vector2f start, Vector2f end, float distanceFromStart, Vector2f result) {
		dir.set(end);
		dir.sub(start).normalise();
		result.set(start);
		return result.add(dir.scale(distanceFromStart));
	}

	public Vector2f alignementVector(Line line, Vector2f pt) {
		line.getClosestPoint(pt, lineClosesPoint);
		alignmentVector.set(lineClosesPoint);
		alignmentVector.sub(pt);
		return alignmentVector;
	}

}
