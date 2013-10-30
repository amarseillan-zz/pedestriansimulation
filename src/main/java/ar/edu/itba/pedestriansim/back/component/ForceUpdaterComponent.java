package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.DrivingForce;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.RigidBody;
import ar.edu.itba.pedestriansim.back.SpringForceModel;
import ar.edu.itba.pedestriansim.back.Updateable;

public class ForceUpdaterComponent implements Updateable {

	private final static Vector2f nullForce = new Vector2f();
	private final PedestrianArea scene;
	private final DrivingForce forceModel = new DrivingForce();
	private final SpringForceModel collisitionModel = new SpringForceModel(10000);

	public ForceUpdaterComponent(PedestrianArea scene) {
		this.scene = scene;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			Vector2f forces = new Vector2f();
			forces.add(getDesireForce(subject));
			forces.add(getExternalForces(subject));
			subject.applyForce(forces);
		}
	}

	private Vector2f getDesireForce(Pedestrian subject) {
		if (subject.getTarget() != null) {
			RigidBody body = subject.getBody();
			return forceModel.getForce(body, subject.getTarget().getCenter(), subject.getMaxVelocity());
		}
		return nullForce;
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
