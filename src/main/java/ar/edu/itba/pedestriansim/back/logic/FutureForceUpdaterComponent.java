package ar.edu.itba.pedestriansim.back.logic;

import java.util.Map;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.force.RepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

import com.google.common.collect.Maps;

public class FutureForceUpdaterComponent extends PedestrianAreaStep {

	private static final Logger LOGGER = Logger.getLogger(FutureForceUpdaterComponent.class);

	private final PedestrianForces _forces;

	public FutureForceUpdaterComponent(PedestrianForces pedestrianForces) {
		_forces = pedestrianForces;
	}

	@Override
	public void update(PedestrianArea input) {
		Map<Pedestrian, Vector2f> forceOnFuture = Maps.newHashMap();
		RepulsionForce repulsionForce = _forces.getRepulsionForceModel();
		for (Pedestrian subject : input.pedestrians()) {
			for (Pedestrian other : input.pedestrians()) {
				if (subject != other && !isOnBack(subject, other)) {
					Vector2f future1 = subject.getFuture().getBody().getCenter();
					Vector2f future2 = other.getFuture().getBody().getCenter();
					Vector2f repulsion = repulsionForce.apply(future1, future2);
					forceOnFuture.put(subject, repulsion);
					forceOnFuture.put(other, repulsion.copy().scale(-1));
					// for (Shape shape : input.obstacles()) {
					// if (!(shape instanceof Line)) {
					// LOGGER.error("obstacles that are not lines are not yet supported");
					// throw new RuntimeException();
					// }
					// Vector2f closest = new Vector2f();
					// ((Line) shape).getClosestPoint(futureLocation, closest);
					// Vector2f repulsionForce =
					// _pedestrianForces.getRepulsionForceModel().apply(futureLocation,
					// closest);
					// externalForcesOnFuture.add(repulsionForce);
					// }
				}
			}
		}
		for (Pedestrian subject : input.pedestrians()) {
			updatePedestrianFuture(input, subject, forceOnFuture);
		}
	}

	private void updatePedestrianFuture(PedestrianArea input, Pedestrian pedestrian, Map<Pedestrian, Vector2f> allForcesOnFuture) {
		Vector2f forceOnFuture = allForcesOnFuture.get(pedestrian);
		float threshold = _forces.getExternalForceThreshold();
		if (forceOnFuture == null || forceOnFuture.lengthSquared() < (threshold * threshold)) {
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
		Vector2f p1Center = p1.getBody().getCenter();
		Vector2f f1Center = p1.getFuture().getBody().getCenter();
		Vector2f horizontalDir = new Vector2f(f1Center).sub(p1Center).add(90);
		Vector2f p2Center = p2.getBody().getCenter();
		return Vectors.isLeft(p1Center, horizontalDir.add(p1Center), p2Center);
	}

	private final Vector2f cache = new Vector2f();

	private void setFutureInDesiredPath(Pedestrian me) {
		Vector2f targetCenter = me.getTargetSelection().getTarget().getClosesPoint(me.getBody().getCenter());
		float distance = me.getReactionDistance();
		if (me.getBody().getCenter().distanceSquared(targetCenter) < distance * distance) {
			cache.set(targetCenter);
		} else {
			Vectors.pointBetween(me.getBody().getCenter(), targetCenter, distance, cache);
		}
		me.getFuture().getBody().setLocation(cache);
		me.getFuture().getBody().setAppliedForce(Vectors.nullVector());
		me.getFuture().getBody().setVelocity(Vectors.nullVector());
	}
}
