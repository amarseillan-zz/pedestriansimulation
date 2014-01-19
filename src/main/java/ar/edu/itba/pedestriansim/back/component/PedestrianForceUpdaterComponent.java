package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianForces;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.physics.SpringForceModel;

public class PedestrianForceUpdaterComponent implements Updateable {

	private static final Vector2f nullForce = new Vector2f();
	
	private final Vector2f forces = new Vector2f();
	private final PedestrianArea scene;
	private final PedestrianForces _pedestrianForces;

	public PedestrianForceUpdaterComponent(PedestrianArea scene, PedestrianForces pedestrianForces) {
		this.scene = scene;
		_pedestrianForces = pedestrianForces;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			forces.set(nullForce);
			forces.add(_pedestrianForces.getDesireForce().apply(subject));
			forces.add(getExternalForces(subject));
			subject.getBody().applyForce(forces);
		}
	}

	private Vector2f getExternalForces(Pedestrian subject) {
		Vector2f externalForces = new Vector2f();
		SpringForceModel collisitionModel = _pedestrianForces.getCollisitionModel();
		for (Pedestrian other : scene.getOtherPedestrians(subject)) {
			externalForces.add(collisitionModel.getForce(subject.getBody().getCollitionShape(), other.getBody().getCollitionShape()));
		}
		for (Shape shape : scene.getObstacles()) {
			Vector2f force = collisitionModel.getForce(subject.getBody().getCollitionShape(), shape);
			externalForces.add(force);
		}
		return externalForces;
	}
}
