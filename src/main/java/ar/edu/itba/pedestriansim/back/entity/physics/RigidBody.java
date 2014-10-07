package ar.edu.itba.pedestriansim.back.entity.physics;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

public class RigidBody {

	private Vector2f _center;
	private Vector2f _velocity;
	private Vector2f _appliedForce;
	private float _mass;
	private float _radius;
	private Circle _shape;

	public RigidBody(float mass, Vector2f location, float radius) {
		_mass = mass;
		_center = new Vector2f(location);
		_velocity = new Vector2f();
		_appliedForce = new Vector2f();
		_radius = radius;
		_shape = new Circle(location.x, location.y, radius);
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
		return _radius;
	}

	public void setCenter(Vector2f center) {
		_center.set(center);
		recenterShape();
	}

	public Circle getShape() {
		return _shape;
	}

	private void recenterShape() {
		_shape.setCenterX(getCenter().x);
		_shape.setCenterY(getCenter().y);
		_shape.setRadius(getRadius());
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
		recenterShape();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("center", getCenter())
			.append("velocity", getVelocity())
			.append("force", getAppliedForce())
			.build();
	}
}
