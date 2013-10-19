package ar.edu.itba.pedestriansim.back;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import ar.edu.itba.pedestriansim.back.component.ForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PositionUpdaterComponent;

public class PedestrianSim implements Updateable {

	private PedestrianArea scene;
	private List<Updateable> updatables = new LinkedList<Updateable>();

	public PedestrianSim() {
		scene = new PedestrianArea();
		updatables.add(new ForceUpdaterComponent(scene));
		updatables.add(new PositionUpdaterComponent(scene));
	}

	public void addPedestrian(Pedestrian pedestrian) {
		scene.addPedestrian(pedestrian);
	}
	
	public void update(GameContainer gc, float elapsedTimeInSeconds) {
		for (Updateable updatable : updatables) {
			updatable.update(gc, elapsedTimeInSeconds);
		}
	}

	public PedestrianArea getScene() {
		return scene;
	}

}
