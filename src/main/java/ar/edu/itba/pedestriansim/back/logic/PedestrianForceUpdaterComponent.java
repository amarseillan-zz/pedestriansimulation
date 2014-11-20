package ar.edu.itba.pedestriansim.back.logic;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.Wall;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class PedestrianForceUpdaterComponent extends PedestrianAreaStep {

	private final Vector2f forces = new Vector2f();
	private final PedestrianForces _pedestrianForces;

	public PedestrianForceUpdaterComponent(PedestrianForces pedestrianForces) {
		_pedestrianForces = pedestrianForces;
	}

	@Override
	public void update(PedestrianArea input) {
		for (Pedestrian subject : input.pedestrians()) {
			forces.set(Vectors.zero());
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
		for (Wall wall : input.obstacles()) {
			externalForces.add(collitionModel.getForce(subject.getBody(), wall.line()));
		}
		return externalForces;
	}
}
