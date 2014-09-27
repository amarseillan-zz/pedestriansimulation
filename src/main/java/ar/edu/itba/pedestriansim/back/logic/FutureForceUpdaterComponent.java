package ar.edu.itba.pedestriansim.back.logic;

import java.util.Map;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.force.RepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

import com.google.common.collect.Maps;

public class FutureForceUpdaterComponent extends PedestrianAreaStep {

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
			for (Pedestrian other : input.pedestrians()) {
				if (subject != other && !isOnBack(subject, other)) {
					Vector2f future1 = subject.getFuture().getBody().getCenter();
					Vector2f future2 = other.getFuture().getBody().getCenter();
					repulsion.add(repulsionForce.between(future1, future2, subject.pedestrianRepulsionForceValues()));
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

	private Vector2f obstacleCollitionForces(Pedestrian subjet, PedestrianArea input) {
		final Vector2f totalRepulsionForce = new Vector2f();
		RepulsionForce repulsionForce = _forces.getWallRepulsionForceModel();
		for (Shape shape : input.obstacles()) {
			if (!(shape instanceof Line)) {
				logger.error("obstacles that are not lines are not yet supported");
				throw new RuntimeException();
			}
			Line line = (Line) shape;
			RigidBody future = subjet.getFuture().getBody();
			line.getClosestPoint(future.getCenter(), _wallClosestPointCache);
			totalRepulsionForce
				.add(_forces.getCollisitionModel().getForce(future.getCollitionShape(), line))
				.add(repulsionForce.between(future.getCenter(), _wallClosestPointCache, subjet.wallRepulsionForceValues()))
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

	private boolean futureIsFurtherThan(float distance, Pedestrian me) {
		Vector2f futureLocation = me.getFuture().getBody().getCenter();
		float radiusSum = me.getBody().getRadius() + me.getFuture().getBody().getRadius();
		return (me.getBody().getCenter().distance(futureLocation) - radiusSum) > distance;
	}

	private boolean isOnBack(Pedestrian p1, Pedestrian p2) {
//		Vector2f p1Center = p1.getBody().getCenter();
//		Vector2f p1f1 = p1.getFuture().getBody().getCenter().copy().sub(p1Center);
//		Vector2f p1p2 = p2.getBody().getCenter().copy().sub(p1Center);
//		return abs(p1f1.getTheta() - p1p2.getTheta()) > 90;
		return false;
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
