package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.Vectors;
import ar.edu.itba.pedestriansim.back.replusionforce.RepulsionForce;

import com.google.common.base.Function;

public class FutureForceUpdaterComponent implements Updateable {

	private final Vectors vectors = new Vectors();

	private final Vector2f externalForcesOnFuture = new Vector2f();
	private final Vector2f pointAtReactionDistance = new Vector2f();

	private final PedestrianArea scene;
	private final RepulsionForce _repulsionForceModel;
	private final Function<Pedestrian, Vector2f> _interactionLocation;
	private float _externalForceThreshold;
	
	public FutureForceUpdaterComponent(PedestrianArea scene, Function<Pedestrian, Vector2f> interactionLocation, float externalForceThreshold, RepulsionForce repulsionForceModel) {
		this.scene = scene;
		_interactionLocation = interactionLocation;
		_externalForceThreshold = externalForceThreshold;
		_repulsionForceModel = repulsionForceModel;
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
			if (_externalForceThreshold <= externalForceMod) {
				subject.getFuture().getBody().applyForce(externalForcesOnFuture);
			} else {
				subject.getFuture().getBody().applyForce(Vectors.nullVector());
				subject.getFuture().getBody().setVelocity(Vectors.nullVector());
			}
		} else {
			futureLocation.set(center);
		}
	}
}
