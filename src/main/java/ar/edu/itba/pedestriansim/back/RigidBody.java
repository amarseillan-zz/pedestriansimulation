package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class RigidBody {

	private Vector2f _center;
	private float _mass;
	private Vector2f _velocity;
	private CircularShape _collitionShape;

	public RigidBody(float mass, Vector2f location, float radius) {
		_mass = mass;
		_center = location;
		_velocity = new Vector2f();
		_collitionShape = new CircularShape(this, radius);
	}

	public float getMass() {
		return _mass;
	}

	public void setMass(float mass) {
		this._mass = mass;
	}

	public Vector2f getVelocity() {
		return _velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this._velocity = velocity;
	}

	public Vector2f getCenter() {
		return _center;
	}

	public float getRadius() {
		return _collitionShape.getRadius();
	}
	
	public void setLocation(Vector2f location) {
		this._center = location;
	}

	public CircularShape getCollitionShape() {
		return _collitionShape;
	}

	public void apply(Vector2f deltaVelocity, Vector2f deltaPosition) {
		getVelocity().add(deltaVelocity);
		getCenter().add(deltaPosition);
	}
}
