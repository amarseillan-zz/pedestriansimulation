package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.HelbingForceModel;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.RigidBody;
import ar.edu.itba.pedestriansim.back.SpringForceModel;
import ar.edu.itba.pedestriansim.back.Updateable;

public class ForceUpdaterComponent implements Updateable {

	private final HelbingForceModel forceModel = new HelbingForceModel();
	private final SpringForceModel collisitionModel = new SpringForceModel();
	private PedestrianArea scene;

	public ForceUpdaterComponent(PedestrianArea scene) {
		this.scene = scene;
	}

	public void update(GameContainer gc, float elapsedTimeInSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			if (subject.onTarget()) {
				subject.stop();
				continue;
			}
			Vector2f forces = new Vector2f();
			forces.add(getDesireForce(subject));
			forces.add(getExternalForces(subject));
			subject.applyForce(forces);
		}
	}

	private Vector2f getDesireForce(Pedestrian subject) {
		RigidBody body = subject.getBody();
		return forceModel.getForce(body, subject.getTarget(), subject.getMaxVelocity());
	}

	private Vector2f getExternalForces(Pedestrian subject) {
		Vector2f externalForces = new Vector2f();
		for (Pedestrian other : scene.getPedestrians()) {
			if (other == subject) {
				continue;
			}
			externalForces.add(collisitionModel.getForce(subject.getCollitionShape(), other.getCollitionShape()));
		}
		return externalForces;
	}
}
