package ar.edu.itba.pedestriansim.back.mision;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class PedestrianTargetArea implements PedestrianTarget {

	private Shape _targetArea;
	private Vector2f center;

	public PedestrianTargetArea(Shape targetArea) {
		_targetArea = targetArea;
		center = new Vector2f(targetArea.getCenter());
	}

	@Override
	public boolean intersects(Shape shape) {
		return _targetArea.intersects(shape);
	}

	@Override
	public float distanceTo(Vector2f vector) {
		return getCenter().distance(vector);
	}

	@Override
	public Vector2f getCenter() {
		return center;
	}

}
