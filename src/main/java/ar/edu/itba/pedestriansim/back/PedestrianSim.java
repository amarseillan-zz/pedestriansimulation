package ar.edu.itba.pedestriansim.back;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import ar.edu.itba.pedestriansim.back.component.PedestrianForceComponent;

public class PedestrianSim implements Updateable {

	private PedestrianArea scene;
	private List<Updateable> updatables = new LinkedList<Updateable>();

	public PedestrianSim() {
		scene = new PedestrianArea();
		updatables.add(new PedestrianForceComponent(scene));
	}

	public void addPedestrian(Pedestrian pedestrian) {
		scene.addPedestrian(pedestrian);
	}
	
	public void update(GameContainer gc, long elapsedTime) {
		for (Updateable updatable : updatables) {
			updatable.update(gc, elapsedTime);
		}
	}

	public PedestrianArea getScene() {
		return scene;
	}

}
