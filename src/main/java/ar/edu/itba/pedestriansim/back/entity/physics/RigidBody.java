package ar.edu.itba.pedestriansim.back.entity.physics;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Vector2f;

public class RigidBody {

	private Vector2f _center;
	private float _mass;
	private Vector2f _velocity;
	private Vector2f _appliedForce;
	private CircularShape _collitionShape;

	public RigidBody(float mass, Vector2f location, float radius) {
		_mass = mass;
		_center = new Vector2f(location);
		_velocity = new Vector2f();
		_appliedForce = new Vector2f();
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
		this._velocity.set(velocity);
	}

	public Vector2f getCenter() {
		return _center;
	}

	public float getRadius() {
		return _collitionShape.getRadius();
	}

	public void setLocation(Vector2f location) {
		_center.set(location);
	}

	public CircularShape getCollitionShape() {
		return _collitionShape;
	}

	public void setAppliedForce(Vector2f force) {
		_appliedForce.set(force);
	}

	public Vector2f getAppliedForce() {
		return _appliedForce;
	}

	public void apply(Vector2f deltaVelocity, Vector2f deltaPosition) {
		getVelocity().add(deltaVelocity);
		getCenter().add(deltaPosition);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("cener", getCenter())
			.append("velocity", getVelocity())
			.append("force", getAppliedForce())
			.build();
	}
}
