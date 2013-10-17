package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class Pedestrian {

	private static final float MASS = 1;

	private Vector2f _location;
	private float _mass;
	private Vector2f _velocity;
	private Vector2f _target;
	private CircularShape collitionShape;

	public Pedestrian(Vector2f location, Vector2f target) {
		this(MASS, location, target);
	}

	public Pedestrian(float mass, Vector2f location, Vector2f target) {
		_location = location;
		_mass = mass;
		_velocity = new Vector2f();
		_target = target;
		collitionShape = new CircularShape(this);
	}

	public Vector2f getLocation() {
		return _location;
	}

	public Vector2f getVelocity() {
		return _velocity;
	}

	public float getMass() {
		return _mass;
	}

	public Vector2f getTarget() {
		return _target;
	}
	
	public void stop() {
		_velocity.set(0,  0);
	}
	
	public boolean onTarget() {
		return _location.distanceSquared(_target) < 10;
	}

	public void translate(Vector2f distance, long elapsedTime) {
		_location.add(distance);
		_velocity.set(distance);
		_velocity.scale(1.0f / elapsedTime);
	}
	
	public CircularShape getCollitionShape() {
		return collitionShape;
	}
}
