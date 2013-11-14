package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.DrivingForce;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianFutureRepulsionForce;
import ar.edu.itba.pedestriansim.back.RigidBody;
import ar.edu.itba.pedestriansim.back.Updateable;

public class FutureForceUpdaterComponent implements Updateable {

	private static final Vector2f nullForce = new Vector2f();
	
	private final Vector2f forces = new Vector2f();
	private final Vector2f forcesOnFuture = new Vector2f();
	private final PedestrianArea scene;
	private final DrivingForce forceModel = new DrivingForce();
	private final PedestrianFutureRepulsionForce repulsionForce = new PedestrianFutureRepulsionForce();

	public FutureForceUpdaterComponent(PedestrianArea scene) {
		this.scene = scene;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			updatePedestrianFuture(subject);
		}
	}

	private void updatePedestrianFuture(Pedestrian subject) {
		forces.set(nullForce);
		if (subject.getTarget() != null) {
			forces.add(getFutureDesireForce(subject.getFuture().getBody(), subject.getTarget().getCenter()));
		}
		Vector2f future = subject.getFuture().getBody().getCenter();
		forcesOnFuture.set(nullForce);
		for (Pedestrian other : scene.getPedestrians()) {
			if (!other.equals(subject)) {
				Vector2f otherFuture = other.getFuture().getBody().getCenter();
				forcesOnFuture.add(repulsionForce.getForce(future, otherFuture));
			}
		}
		forces.add(forcesOnFuture);
		subject.getFuture().getBody().applyForce(forces);
	}
	
	private Vector2f getFutureDesireForce(RigidBody future, Vector2f target) {
		return forceModel.getForceWithoutVelocity(future, target);
		
	}
}
