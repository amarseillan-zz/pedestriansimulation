package ar.edu.itba.pedestriansim.back.mision;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;

public class GoToSelfLocation implements PedestrianTarget {

	private final Pedestrian _pedestrian;

	public GoToSelfLocation(Pedestrian pedestrian) {
		_pedestrian = pedestrian;
	}

	@Override
	public boolean intersects(Shape shape) {
		return _pedestrian.getShape() == shape;
	}

	@Override
	public float distanceTo(Vector2f pt) {
		return getCenter().distance(pt);
	}

	@Override
	public Vector2f getCenter() {
		return _pedestrian.getBody().getCenter();
	}

}
