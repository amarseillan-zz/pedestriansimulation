package ar.edu.itba.pedestriansim.back.entity.mision;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public interface PedestrianTarget {

	boolean intersects(Shape shape);

	Vector2f getClosesPoint(Vector2f position);

}
