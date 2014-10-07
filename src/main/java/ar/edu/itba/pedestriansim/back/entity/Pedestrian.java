package ar.edu.itba.pedestriansim.back.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Circle;
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
	private final RepulsionForceValues _pedestrianRepulsionForceValues;
	private final RepulsionForceValues _wallRepulsionForceValues;

	public Pedestrian(Integer id, int team, RigidBody body) {
		_id = Preconditions.checkNotNull(id);
		_team = team;
		_body = Preconditions.checkNotNull(body);
		_future = new PedestrianFuture(1, this);
		targetSelection = new TargetSelection(this);
		_pedestrianRepulsionForceValues = new RepulsionForceValues();
		_wallRepulsionForceValues = new RepulsionForceValues();
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
		getFuture().getBody().setCenter(getBody().getCenter());
	}

	public RigidBody getBody() {
		return _body;
	}

	public Circle getShape() {
		return _body.getShape();
	}

	public float getMaxVelocity() {
		return _maxVelocity;
	}

	public void setMaxVelocity(float maxVelocity) {
		_maxVelocity = maxVelocity;
	}

	public void setCenter(Vector2f center) {
		_future.getBody().setCenter(center);
		_body.setCenter(center);
	}

	public final void setReactionDistance(float reactionDistance) {
		_reactionDistance = reactionDistance;
	}

	public void recenterFuture() {
		Vector2f targetCenter = getTargetSelection().getTarget().getClosesPoint(getBody().getCenter());
		float distance = _reactionDistance;
		Vector2f cache = new Vector2f();
		Vectors.pointBetween(getBody().getCenter(), targetCenter, distance, cache);
		getFuture().getBody().setCenter(cache);
		
	}

	public float getReactionDistance() {
		return _reactionDistance;
	}

	public TargetSelection getTargetSelection() {
		return targetSelection;
	}

	public RepulsionForceValues pedestrianRepulsionForceValues() {
		return _pedestrianRepulsionForceValues;
	}
	
	public RepulsionForceValues wallRepulsionForceValues() {
		return _wallRepulsionForceValues;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("team", getTeam())
				.append("location", getBody().getCenter()).append("velocity", getBody().getVelocity())
				.append("target(center)", getTargetSelection().getTarget().getClosesPoint(getBody().getCenter())).build();
	}
}
