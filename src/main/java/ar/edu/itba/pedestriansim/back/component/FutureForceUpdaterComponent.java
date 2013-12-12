package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianFutureRepulsionForceModel;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.Vectors;

public class FutureForceUpdaterComponent implements Updateable {

	private final static float EXTERNAL_FORCE_THRESHOLD_NEWTON = 5;
	private final PedestrianArea scene;
	private final PedestrianFutureRepulsionForceModel repulsionForceModel = new PedestrianFutureRepulsionForceModel();
	private final Vectors vectors = new Vectors();

	private final Vector2f externalForcesOnFuture = new Vector2f();
	private final Vector2f pointAtReactionDistance = new Vector2f();

	public FutureForceUpdaterComponent(PedestrianArea scene) {
		this.scene = scene;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			updatePedestrianFuture(subject);
		}
	}

	private void updatePedestrianFuture(Pedestrian subject) {
		Vector2f futureLocation = subject.getFuture().getBody().getCenter();
		Vector2f center = subject.getBody().getCenter();
		if (!subject.isOnTarget()) {
			Vector2f target = subject.getTarget().getCenter();
			vectors.pointBetween(center, target, subject.getReactionDistance(), pointAtReactionDistance);
			futureLocation.set(pointAtReactionDistance);
			externalForcesOnFuture.set(Vectors.nullVector());
			for (Pedestrian other : scene.getOtherPedestrians(subject)) {
				Vector2f otherFuture = _interactionLocation.apply(other);
				externalForcesOnFuture.add(_repulsionForceModel.apply(futureLocation, otherFuture));
			}
			float externalForceMod = externalForcesOnFuture.length();
			if (EXTERNAL_FORCE_THRESHOLD_NEWTON <= externalForceMod) {
				subject.getFuture().getBody().applyForce(externalForcesOnFuture);
			} else {
				subject.getFuture().getBody().applyForce(Vectors.nullVector());
				subject.getFuture().getBody().setVelocity(Vectors.nullVector());
			}
		} else {
			futureLocation.set(center);
		}
	}

	private Vector2f getPedestrianFutureLocation(Pedestrian pedestrian) {
		return pedestrian.getFuture().getBody().getCenter();
	}
}
