package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class Pedestrian {

	private static final float DEFAULT_MASS = 60;

	private Vector2f _target;
	private Vector2f _appliedForce;
	private RigidBody _body;
	private CircularShape collitionShape;
	private float _maxVelocity;

	public Pedestrian(Vector2f location, Vector2f target) {
		this(DEFAULT_MASS, location, target);
	}

	public Pedestrian(float mass, Vector2f location, Vector2f target) {
		_body = new RigidBody(mass, location);
		_target = target;
		collitionShape = new CircularShape(this);
		_maxVelocity = 50;
		_appliedForce = new Vector2f();
	}

	public Vector2f getTarget() {
		return _target;
	}
	
	public void applyForce(Vector2f force) {
		_appliedForce.set(force);
	}

	public Vector2f getAppliedForce() {
		return _appliedForce;
	}

	public void stop() {
		_body.getVelocity().set(0, 0);
	}

	public boolean onTarget() {
		return _body.getLocation().distanceSquared(_target) < 1;
	}

	public CircularShape getCollitionShape() {
		return collitionShape;
	}
	
	public RigidBody getBody() {
		return _body;
	}
	
	public float getMaxVelocity() {
		return _maxVelocity;
	}
	
	public void setMaxVelocity(float maxVelocity) {
		_maxVelocity = maxVelocity;
	}
}
