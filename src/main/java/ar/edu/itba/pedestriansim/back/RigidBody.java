package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class RigidBody {

	private Vector2f _location;
	private float _mass;
	private Vector2f _velocity;

	public RigidBody(float mass, Vector2f location) {
		_mass = mass;
		_location = location;
		_velocity = new Vector2f();
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

	public Vector2f getLocation() {
		return _location;
	}

	public void setLocation(Vector2f location) {
		this._location = location;
	}
	
	public void apply(Vector2f deltaVelocity, Vector2f deltaPosition) {
		getVelocity().add(deltaVelocity);
		getLocation().add(deltaPosition);
	}
}
