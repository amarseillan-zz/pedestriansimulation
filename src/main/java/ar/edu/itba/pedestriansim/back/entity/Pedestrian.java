package ar.edu.itba.pedestriansim.back.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

import com.google.common.base.Preconditions;

public class Pedestrian {

	public static final float DEFAULT_REACTION_DISTANCE = 1.5f;
	
	private Integer _id;
	private int _team;
	private TargetSelection targetSelection;
	private PedestrianMision _mission;
	private PedestrianFuture _future;
	private RigidBody _body;
	private float _maxVelocity;
	private float _reactionDistance;

	public Pedestrian(Integer id, int team, RigidBody body) {
		_id = Preconditions.checkNotNull(id);
		_team = team;
		_body = Preconditions.checkNotNull(body);
		_future = new PedestrianFuture(1, this);
		targetSelection = new TargetSelection(this);
	}

	public void setMission(PedestrianMision mission) {
		_mission = Preconditions.checkNotNull(mission);
	}

	public Integer getId() {
		return _id;
	}

	public int getTeam() {
		return _team;
	}
	
	public PedestrianMision getMission() {
		return _mission;
	}

	public PedestrianFuture getFuture() {
		return _future;
	}

	public void stop() {
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
	
	public final void setReactionDistance(float reactionDistance) {
		_reactionDistance = reactionDistance;
		Vector2f targetCenter = getTargetSelection().getTarget().getClosesPoint(getBody().getCenter());
		float distance = _reactionDistance;
		Vector2f cache = new Vector2f();
		Vectors.pointBetween(getBody().getCenter(), targetCenter, distance, cache);
		getFuture().getBody().setLocation(cache);
	}

	public float getReactionDistance() {
		return _reactionDistance;
	}
	
	public TargetSelection getTargetSelection() {
		return targetSelection;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", getId())
			.append("team", getTeam())
			.append("location", getBody().getCenter())
			.append("velocity", getBody().getVelocity())
			.append("target(center)", getTargetSelection().getTarget().getClosesPoint(getBody().getCenter()))
			.build();
	}
}
