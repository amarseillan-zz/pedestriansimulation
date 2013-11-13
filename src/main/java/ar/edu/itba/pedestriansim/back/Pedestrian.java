package ar.edu.itba.pedestriansim.back;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Shape;

public class Pedestrian {

	private Serializable _id;
	private int _team;
	private PedestrianTargetList _targetList;
	private PedestrianTargetArea _currentTarget;
	private PedestrianFuture _future;
	private RigidBody _body;
	private float _maxVelocity;

	public Pedestrian(Serializable id, PedestrianTargetList targets, int team, RigidBody body) {
		_id = id;
		_team = team;
		_targetList = targets;
		_body = body;
		_currentTarget = _targetList.getFirst();
		_future = new PedestrianFuture(this);
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
		if (_currentTarget == null || _currentTarget.onTarget(getShape())) {
			return 0;
		} else {
			float velocity = getBody().getVelocity().length();
			return Math.abs(velocity) < 0.01 ? Float.NaN  : getTarget().distanceTo(getBody().getCenter()) / velocity;
		}
	}

	public boolean isOnFinalTarget() {
		if (_currentTarget == null) {
			return true;
		}
		return _currentTarget.onTarget(getShape()) && !_targetList.hasNextTarget(_currentTarget);
	}

	public void updateTarget() {
		if (_currentTarget != null && _currentTarget.onTarget(getShape())) {
			_currentTarget = _targetList.nextTarget(_currentTarget);
		}
		if (_currentTarget == null) {
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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", getId())
			.append("location", getBody().getCenter())
			.append("target", getBody().getVelocity())
			.build();
	}
}
