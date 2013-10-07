package ar.edu.itba.pedestriansim.back;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import ar.edu.itba.pedestriansim.back.component.ExternalForcesComponent;
import ar.edu.itba.pedestriansim.back.component.PositionUpdater;

public class PedestrianSim implements Updateable {

	private Scene scene;
	private List<Updateable> updatables = new LinkedList<Updateable>();

	public PedestrianSim() {
		scene = new Scene();
		scene.addPedestrian(new Pedestrian(100, 100));
		updatables.add(new ExternalForcesComponent(scene));
		updatables.add(new PositionUpdater(scene));
	}

	public void update(GameContainer gc, float elapsedTimeSeconds) {
		for (Updateable updatable : updatables) {
			updatable.update(gc, elapsedTimeSeconds);
		}
	}

	public Scene getScene() {
		return scene;
	}

}
