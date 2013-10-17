package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;

public class PedestrianForceComponent implements Updateable {

	private float MAX_VELOCITY = 0.5f;
	private PedestrianArea scene;

	public PedestrianForceComponent(PedestrianArea scene) {
		this.scene = scene;
	}

	public void update(GameContainer gc, long elapsedTime) {
		Vector2f subjectVelocity = new Vector2f();
		for (Pedestrian subject : scene.getPedestrians()) {
			if (subject.onTarget()) {
				subject.stop();
				continue;
			}
			Vector2f forces = new Vector2f();
			forces.add(getDesireForce(subject, elapsedTime));
			forces.add(getExternalForces(subject));
			subjectVelocity.set(subject.getVelocity());
			subjectVelocity.add(forces.scale(elapsedTime / subject.getMass()));
			Vector2f dx = subjectVelocity.scale(elapsedTime);
			subject.translate(dx, elapsedTime);
		}
	}

	private Vector2f getDesireForce(Pedestrian subject, long elapsedTime) {
		Vector2f forceDir = new Vector2f(subject.getTarget()).sub(subject.getLocation()).normalise();		
		Vector2f velocity = forceDir.scale(MAX_VELOCITY);
		velocity.sub(subject.getVelocity());
		return velocity.scale(subject.getMass() / elapsedTime);
	}
	
	private Vector2f getExternalForces(Pedestrian subject) {
		Vector2f externalForces = new Vector2f();
		for (Pedestrian other : scene.getPedestrians()) {
			if (other == subject) {
				continue;
			}
			if (other.getCollitionShape().isCollidingWith(other.getCollitionShape())) {
				// TODO: calcular la fuerza de repulsion
			}
		}
		return externalForces;
	}
}
