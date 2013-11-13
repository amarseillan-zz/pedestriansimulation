package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.DrivingForce;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianFutureRepulsionForce;
import ar.edu.itba.pedestriansim.back.SpringForceModel;
import ar.edu.itba.pedestriansim.back.Updateable;

public class ForceUpdaterComponent implements Updateable {

	private static final Vector2f nullForce = new Vector2f();
	
	private final Vector2f forces = new Vector2f();
	private final Vector2f forcesOnFuture = new Vector2f();
	private final PedestrianArea scene;
	private final DrivingForce forceModel = new DrivingForce();
	private final SpringForceModel collisitionModel = new SpringForceModel(10000);
	private final PedestrianFutureRepulsionForce repulsionForce = new PedestrianFutureRepulsionForce();

	public ForceUpdaterComponent(PedestrianArea scene) {
		this.scene = scene;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			updatePedestrianFuture(subject);
			// Pedestrian forces
			forces.set(nullForce);
			forces.add(getDesireForce(subject, subject.getFuture().getBody().getCenter()));
			forces.add(getExternalForces(subject));
			subject.getBody().applyForce(forces);
		}
	}

	private void updatePedestrianFuture(Pedestrian subject) {
		forces.set(nullForce);
		if (subject.getTarget() != null) {
			forces.add(getDesireForce(subject, subject.getTarget().getCenter()));
		}
		Vector2f center = subject.getBody().getCenter();
		forcesOnFuture.set(nullForce);
		for (Pedestrian other : scene.getPedestrians()) {
			if (!other.equals(subject)) {
				Vector2f otherCenter = other.getBody().getCenter();
				forcesOnFuture.add(repulsionForce.getForce(center, otherCenter));
			}
		}
		forces.add(forcesOnFuture);
		subject.getFuture().getBody().applyForce(forces);
	}
	
	private Vector2f getDesireForce(Pedestrian subject, Vector2f target) {
		return forceModel.getForce(subject.getBody(), target, subject.getMaxVelocity());
	}

	private Vector2f getExternalForces(Pedestrian subject) {
		Vector2f externalForces = new Vector2f();
		for (Pedestrian other : scene.getPedestrians()) {
			if (other != subject) {
				externalForces.add(collisitionModel.getForce(subject.getBody().getCollitionShape(), other.getBody().getCollitionShape()));
			}
		}
		for (Shape shape : scene.getObstacles()) {
			Vector2f force = collisitionModel.getForce(subject.getBody().getCollitionShape(), shape);
			externalForces.add(force);
		}
		return externalForces;
	}
}
