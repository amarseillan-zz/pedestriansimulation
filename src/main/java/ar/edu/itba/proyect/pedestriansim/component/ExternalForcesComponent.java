package ar.edu.itba.proyect.pedestriansim.component;

import java.util.List;

import org.newdawn.slick.GameContainer;

import ar.edu.itba.proyect.pedestriansim.Force;
import ar.edu.itba.proyect.pedestriansim.Pedestrian;
import ar.edu.itba.proyect.pedestriansim.Scene;

public class ExternalForcesComponent implements Updateable {

	private Scene scene;

	public ExternalForcesComponent(Scene scene) {
		this.scene = scene;
	}

	public void update(GameContainer gc, float elapsedTimeSeconds) {
		List<Pedestrian> pedestrians = scene.getPedestrians();
		for (int i = 0; i < pedestrians.size(); i++) {
			Pedestrian subject = pedestrians.get(i);
			Force resulting = new Force();
			for (int j = 0; j < pedestrians.size(); j++) {
				if (i == j) {
					continue;
				}
				Pedestrian other = pedestrians.get(j);
				resulting.add(calculateInteractionForce(subject, other));
			}
			subject.apply(resulting);
		}
	}

	private Force calculateInteractionForce(Pedestrian subject, Pedestrian other) {
		// TODO: implement this
		return new Force();
	}

}
