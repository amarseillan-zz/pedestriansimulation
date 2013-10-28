package ar.edu.itba.pedestriansim.back;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.rand.RandomGenerator;
import ar.edu.itba.pedestriansim.back.rand.UniformRandomGenerator;

public class Pedestrian {

	private static final float TARGET_MIN_DISTANCE_SQ = 1.0f * 1.0f;
	private static final RandomGenerator _massGenerator = new UniformRandomGenerator(50, 80);
	private static final RandomGenerator _velocityGenerator = new UniformRandomGenerator(1f, 1.6f);
	private static final RandomGenerator _radiusGenerator = new UniformRandomGenerator(0.25f, 0.29f);

	private Serializable _id;
	private int _team;
	private PedestrianMovementStrategy _strategy;
	private Vector2f _appliedForce;
	private RigidBody _body;
	private float _maxVelocity;

	public Pedestrian(Serializable id, Vector2f location, PedestrianMovementStrategy strategy, int team) {
		_id = id;
		_team = team;
		_strategy = strategy;
		_appliedForce = new Vector2f();
		_body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		setMaxVelocity(_velocityGenerator.generate());
	}

	public Serializable getId() {
		return _id;
	}

	public int getTeam() {
		return _team;
	}

	public Vector2f getTarget() {
		return _strategy.currentTarget();
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
			return Math.abs(velocity) < 0.01 ? Float.NaN  : getBody().getCenter().distance(getTarget()) / velocity;
		}
	}

	public boolean onTarget() {
		Vector2f target = getTarget();
		if (target == null) {
			return true;
		}
		if (getBody().getCenter().distanceSquared(target) < TARGET_MIN_DISTANCE_SQ) {
			if (!_strategy.hasNextTarget()) {
				return true;
			}
			_strategy.nextTarget();
		}
		return false;
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
			.append("location", getBody().getCenter())
			.append("target", getBody().getVelocity())
			.build();
	}
}
