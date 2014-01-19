package ar.edu.itba.pedestriansim.back.mision;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public interface PedestrianTarget {

	boolean intersects(Shape shape);

	float distanceTo(Vector2f pt);

	Vector2f getCenter();

}
