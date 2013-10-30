package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class PedestrianTargetArea {

	private Line _target;
	private Vector2f center;

	public PedestrianTargetArea(Line target) {
		_target = target;
		center = new Vector2f(target.getCenter());
	}

	public boolean onTarget(Shape shape) {
		return _target.intersects(shape);
	}
	
	public float distanceTo(Vector2f vector) {
		return _target.distance(vector);
	}
	
	public Vector2f getCenter() {
		return center;
	}
	
	public Shape getShape() {
		return _target;
	}
	
}
