package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

public class CircularShape {

	private RigidBody _body;
	private Circle _shape;

	public CircularShape(RigidBody body, float radius) {
		_body = body;
		_shape = new Circle(0, 0, radius);
	}

	public boolean isCollidingWith(CircularShape other) {
		recenterShape();
		other.recenterShape();
		return _shape.intersects(other._shape) || _shape.contains(other._shape)
			|| other._shape.contains(_shape);
	}

	private void recenterShape() {
		_shape.setCenterX(_body.getCenter().x);
		_shape.setCenterY(_body.getCenter().y);
		_shape.setRadius(getRadius());
	}

	public Vector2f getCenter() {
		return new Vector2f(_shape.getCenter());
	}

	public float getRadius() {
		return _shape.getRadius();
	}

	public Circle getShape() {
		return _shape;
	}
}
