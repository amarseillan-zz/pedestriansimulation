package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.SpringForceModel;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.desireforce.DesireForce;

public class PedestrianForceUpdaterComponent implements Updateable {

	private static final Vector2f nullForce = new Vector2f();
	
	private final Vector2f forces = new Vector2f();
	private final PedestrianArea scene;
	private final DesireForce _desireForce;
	private final SpringForceModel _collisitionModel;

	public PedestrianForceUpdaterComponent(PedestrianArea scene, DesireForce desireForce, SpringForceModel collisitionModel) {
		this.scene = scene;
		_desireForce = desireForce;
		_collisitionModel = collisitionModel;
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			forces.set(nullForce);
			forces.add(_desireForce.exertedBy(subject));
			forces.add(getExternalForces(subject));
			subject.getBody().applyForce(forces);
		}
	}

	private Vector2f getExternalForces(Pedestrian subject) {
		Vector2f externalForces = new Vector2f();
		for (Pedestrian other : scene.getOtherPedestrians(subject)) {
			externalForces.add(_collisitionModel.getForce(subject.getBody().getCollitionShape(), other.getBody().getCollitionShape()));
		}
		for (Shape shape : scene.getObstacles()) {
			Vector2f force = _collisitionModel.getForce(subject.getBody().getCollitionShape(), shape);
			externalForces.add(force);
		}
		externalForces.add(subject.getFuture().getBody().getAppliedForce());
		return externalForces;
	}
}
