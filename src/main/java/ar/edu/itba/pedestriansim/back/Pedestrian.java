package ar.edu.itba.pedestriansim.back;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Vector2f;

public class Pedestrian {

	private Serializable _id;
	private Vector2f _target;
	private Vector2f _appliedForce;
	private RigidBody _body;
	private CircularShape collitionShape;
	private float _maxVelocity;

	public Pedestrian(Serializable id, float mass, Vector2f location, Vector2f target) {
		_body = new RigidBody(mass, location);
		_target = target;
		collitionShape = new CircularShape(this);
		_appliedForce = new Vector2f();
		_id = id;
	}

	public Serializable getId() {
		return _id;
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

	public float getETA() {
		if (onTarget()) {
			return 0;
		} else {
			float velocity = getBody().getVelocity().length();
			return Math.abs(velocity) < 0.01 ? Float.NaN  : getBody().getLocation().distance(getTarget()) / velocity;
		}
	}

	public boolean onTarget() {
		return getBody().getLocation().distanceSquared(getTarget()) < 1;
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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", getId())
			.append("location", getBody().getLocation())
			.append("target", getBody().getVelocity())
			.build();
	}
}
