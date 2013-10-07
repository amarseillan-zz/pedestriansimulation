package ar.edu.itba.pedestriansim.back.component;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.Scene;
import ar.edu.itba.pedestriansim.back.Updateable;

public class ExternalForcesComponent implements Updateable {

	private Scene scene;

	public ExternalForcesComponent(Scene scene) {
		this.scene = scene;
	}

	public void update(GameContainer gc, float elapsedTimeSeconds) {
		List<Pedestrian> pedestrians = scene.getPedestrians();
		for (Pedestrian subject : pedestrians) {
			Vector2f velocity = subject.getVelocity();
			Vector2f totalForce = new Vector2f(subject.getDesireVelocity()).sub(velocity);
			for (Pedestrian other : pedestrians) {
				if (other == subject) {
					continue;
				}
				totalForce.add(interactionForce(subject, other));
			}
			Vector2f accel = new Vector2f(totalForce).scale(1 / subject
					.getMass());
			subject.addVelocity(accel.scale(elapsedTimeSeconds));
		}
	}

	private Vector2f interactionForce(Pedestrian subject, Pedestrian other) {
		return new Vector2f();
	}

}
