package ar.edu.itba.pedestriansim.back.mision;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class PedestrianTargetArea implements PedestrianTarget {

	private Line _targetArea;
	private Vector2f _result = new Vector2f();

	public PedestrianTargetArea(Line targetArea) {
		_targetArea = targetArea;
	}

	@Override
	public boolean intersects(Shape shape) {
		return _targetArea.intersects(shape);
	}

	@Override
	public Vector2f getClosesPoint(Vector2f point) {
		_targetArea.getClosestPoint(point, _result);
		return _result;
	}

}
