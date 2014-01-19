package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianForces;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.physics.Vectors;

public class FutureForceUpdaterComponent implements Updateable {

	private final Vector2f externalForcesOnFuture = new Vector2f();

	private final PedestrianArea scene;
	private final PedestrianForces _pedestrianForces;

	public FutureForceUpdaterComponent(PedestrianArea scene, PedestrianForces pedestrianForces) {
		this.scene = scene;
		_pedestrianForces = pedestrianForces;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			updatePedestrianFuture(subject);
		}
	}

	private void updatePedestrianFuture(Pedestrian subject) {
		Vector2f future = subject.getFuture().getBody().getCenter();
		externalForcesOnFuture.set(Vectors.nullVector());
		for (Pedestrian pedestrian : scene.getOtherPedestrians(subject)) {
			Vector2f other = _pedestrianForces.getInteractionLocation().apply(pedestrian);
			Vector2f repulsionForce = _pedestrianForces.getRepulsionForceModel().apply(future, other);
			externalForcesOnFuture.add(repulsionForce);
		}
		float threshold = _pedestrianForces.getExternalForceThreshold();
		if (externalForcesOnFuture.lengthSquared() < threshold * threshold) {
			externalForcesOnFuture.set(Vectors.nullVector());	
		}
		externalForcesOnFuture.add(_pedestrianForces.getForceOnFuture().apply(subject));
		subject.getFuture().getBody().applyForce(externalForcesOnFuture);
	}

}
