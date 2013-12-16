package ar.edu.itba.pedestriansim.back;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Shape;

public class Pedestrian {

	private static final float DEFAULT_REACTION_DISTANCE = 1.5f;
	
	private Serializable _id;
	private int _team;
	private PedestrianTargetList _targetList;
	private PedestrianTargetArea _currentTarget;
	private PedestrianFuture _future;
	private RigidBody _body;
	private float _maxVelocity;
	private float _reactionDistance;

	public Pedestrian(Serializable id, PedestrianTargetList targets, int team, RigidBody body) {
		_id = id;
		_team = team;
		_targetList = targets;
		_body = body;
		_currentTarget = _targetList.getFirst();
		_future = new PedestrianFuture(this);
		setReactionDistance(DEFAULT_REACTION_DISTANCE);
	}

	public Serializable getId() {
		return _id;
	}

	public int getTeam() {
		return _team;
	}

	public PedestrianTargetArea getTarget() {
		return _currentTarget;
	}

	public PedestrianFuture getFuture() {
		return _future;
	}

	public float getETA() {
		if (_currentTarget.onTarget(getShape())) {
			return 0;
		} else {
			float velocity = getBody().getVelocity().length();
			return Math.abs(velocity) < 0.01 ? Float.NaN  : getTarget().distanceTo(getBody().getCenter()) / velocity;
		}
	}

	public boolean isOnTarget() {
		return _currentTarget.onTarget(getShape());
	}
	
	public boolean isOnFinalTarget() {
		return isOnTarget() && !_targetList.hasNextTarget(_currentTarget);
	}

	public void updateTarget() {
		boolean onTarget = _currentTarget.onTarget(getShape()); 
		if (onTarget && _targetList.hasNextTarget(_currentTarget)) {
			_currentTarget = _targetList.nextTarget(_currentTarget);
			onTarget = false;
		}
		if (onTarget) {
			stop();
		}
	}

	private void stop() {
		_body.getVelocity().set(0, 0);
		getFuture().getBody().getCenter().set(getBody().getCenter());
	}

	public RigidBody getBody() {
		return _body;
	}
	
	public Shape getShape() {
		return _body.getCollitionShape().getShape();
	}
	
	public float getMaxVelocity() {
		return _maxVelocity;
	}
	
	public void setMaxVelocity(float maxVelocity) {
		_maxVelocity = maxVelocity;
	}

	public void translate(float dx, float dy) {
		_future.getBody().getCenter().x += dx;
		_future.getBody().getCenter().y += dy;
		_body.getCenter().x += dx;
		_body.getCenter().y += dy;
	}
	
	public void setReactionDistance(float reactionDistance) {
		_reactionDistance = reactionDistance;
	}

	public float getReactionDistance() {
		return _reactionDistance;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", getId())
			.append("location", getBody().getCenter())
			.append("velocity", getBody().getVelocity())
			.append("target(center)", getTarget().getCenter())
			.build();
	}
}
