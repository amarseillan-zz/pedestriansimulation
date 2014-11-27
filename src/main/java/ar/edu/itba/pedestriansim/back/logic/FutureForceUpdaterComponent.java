package ar.edu.itba.pedestriansim.back.logic;

import java.util.Map;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.Wall;
import ar.edu.itba.pedestriansim.back.entity.force.RepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

import com.google.common.collect.Maps;

public class FutureForceUpdaterComponent extends PedestrianAreaStep {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FutureForceUpdaterComponent.class);

	private final PedestrianForces _forces;

	public FutureForceUpdaterComponent(PedestrianForces pedestrianForces) {
		_forces = pedestrianForces;
	}

	@Override
	public void update(PedestrianArea input) {
		Map<Pedestrian, Vector2f> forceOnFuture = Maps.newHashMap();
		RepulsionForce repulsionForce = _forces.getRepulsionForceModel();
		for (Pedestrian subject : input.pedestrians()) {
			Vector2f repulsion = new Vector2f();
			Vector2f future1Center = subject.getFuture().getBody().getCenter();
			Vector2f futureVector1 = subject.getFuture().getBody().getCenter().copy().sub(subject.getBody().getCenter());
			for (Pedestrian other : input.pedestrians()) {
				if (subject != other && !isOnBack(subject, other)) {
					Vector2f future2Center = other.getFuture().getBody().getCenter();
					repulsion.add(
						repulsionForce.between(future1Center, future2Center, subject.pedestrianRepulsionForceValues())
					);
					Vector2f futureVector2 = other.getFuture().getBody().getCenter().copy().sub(other.getBody().getCenter());
					double angle = Vectors.angle(futureVector1, futureVector2);
					if (angle > 120) {
						Vector2f body2Center = other.getBody().getCenter();
						repulsion.add(
							repulsionForce.between(future1Center, body2Center, subject.futurePedestrianRepulsionForceValues())
						);
					}
				}
			}
			repulsion.add(obstacleCollitionForces(subject, input));
			forceOnFuture.put(subject, repulsion);
		}
		for (Pedestrian subject : input.pedestrians()) {
			updatePedestrianFuture(subject, forceOnFuture);
		}
	}

	private static final Vector2f _wallClosestPointCache = new Vector2f();

	private Vector2f obstacleCollitionForces(Pedestrian subject, PedestrianArea input) {
		final Vector2f totalRepulsionForce = new Vector2f();
		RepulsionForce repulsionForce = _forces.getWallRepulsionForceModel();
		for (Wall wall : input.obstacles()) {
			Line line = wall.line();
			RigidBody future = subject.getFuture().getBody();
			line.getClosestPoint(future.getCenter(), _wallClosestPointCache);
			totalRepulsionForce
				.add(_forces.getCollisitionModel().getForce(future, wall))
				.add(repulsionForce.between(future.getCenter(), _wallClosestPointCache, subject.wallRepulsionForceValues()))
			;
		}
		return totalRepulsionForce;
	}

	private void updatePedestrianFuture(Pedestrian pedestrian, Map<Pedestrian, Vector2f> allForcesOnFuture) {
		Vector2f forceOnFuture = allForcesOnFuture.get(pedestrian);
		float threshold = _forces.getExternalForceThreshold();
		if (forceOnFuture.lengthSquared() < (threshold * threshold)) {
			setFutureInDesiredPath(pedestrian);
		} else {
			forceOnFuture.add(_forces.getForceOnFuture().apply(pedestrian));
			pedestrian.getFuture().getBody().setAppliedForce(forceOnFuture);
		}
	}

	@SuppressWarnings("unused")
	private boolean futureIsFurtherThan(float distance, Pedestrian me) {
		Vector2f futureLocation = me.getFuture().getBody().getCenter();
		float radiusSum = me.getBody().getRadius() + me.getFuture().getBody().getRadius();
		return (me.getBody().getCenter().distance(futureLocation) - radiusSum) > distance;
	}

	private boolean isOnBack(Pedestrian subject, Pedestrian other) {
		Vector2f p1Center = subject.getBody().getCenter();
		Vector2f p1f1 = subject.getFuture().getBody().getCenter().copy().sub(p1Center);
		Vector2f p1p2 = other.getBody().getCenter().copy().sub(p1Center);
		return Vectors.angle(p1f1, p1p2) > 90;
	}

	private static final Vector2f _targetCache = new Vector2f();

	public static void setFutureInDesiredPath(Pedestrian me) {
		Vector2f targetCenter = me.getTargetSelection().getTarget().getClosesPoint(me.getBody().getCenter());
		float distance = me.getReactionDistance();
		if (me.getBody().getCenter().distanceSquared(targetCenter) < distance * distance) {
			_targetCache.set(targetCenter);
		} else {
			Vectors.pointBetween(me.getBody().getCenter(), targetCenter, distance, _targetCache);
		}
		me.getFuture().getBody().setCenter(_targetCache);
		me.getFuture().getBody().setAppliedForce(Vectors.zero());
		me.getFuture().getBody().setVelocity(Vectors.zero());
	}
}
