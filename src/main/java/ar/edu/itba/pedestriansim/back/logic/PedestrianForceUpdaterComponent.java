package ar.edu.itba.pedestriansim.back.logic;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;

public class PedestrianForceUpdaterComponent extends PedestrianAreaStep {

	private static final Vector2f nullForce = new Vector2f();

	private final Vector2f forces = new Vector2f();
	private final PedestrianForces _pedestrianForces;

	public PedestrianForceUpdaterComponent(PedestrianForces pedestrianForces) {
		_pedestrianForces = pedestrianForces;
	}

	@Override
	public void update(PedestrianArea input) {
		for (Pedestrian subject : input.pedestrians()) {
			forces.set(nullForce);
			forces.add(_pedestrianForces.getDesireForce().apply(subject));
			forces.add(getExternalForces(input, subject));
			subject.getBody().setAppliedForce(forces);
		}
	}

	private Vector2f getExternalForces(PedestrianArea input, Pedestrian subject) {
		Vector2f externalForces = new Vector2f();
		SpringForceModel collitionModel = _pedestrianForces.getCollisitionModel();
		for (Pedestrian other : input.getPedestriansAndSkip(subject)) {
			externalForces.add(collitionModel.getForce(subject.getBody(), other.getBody()));
		}
		for (Shape obstacle : input.obstacles()) {
			externalForces.add(collitionModel.getForce(subject.getBody(), obstacle));
		}
		return externalForces;
	}
}
