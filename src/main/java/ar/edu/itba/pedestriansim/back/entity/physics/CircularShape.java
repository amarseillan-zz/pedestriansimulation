package ar.edu.itba.pedestriansim.back.entity.physics;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

public class CircularShape {

	private RigidBody _body;
	private Circle _shape;
	private final Vector2f center = new Vector2f();

	public CircularShape(RigidBody body, float radius) {
		_body = body;
		_shape = new Circle(0, 0, radius);
	}

	private void recenterShape() {
		_shape.setCenterX(_body.getCenter().x);
		_shape.setCenterY(_body.getCenter().y);
		_shape.setRadius(getRadius());
		center.set(_shape.getCenter());
	}

	public Vector2f getCenter() {
		return center;
	}

	public float getRadius() {
		return _shape.getRadius();
	}

	public Circle getShape() {
		recenterShape();
		return _shape;
	}
}
