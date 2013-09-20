package ar.edu.itba.proyect.pedestriansim.component;

import org.newdawn.slick.GameContainer;

import ar.edu.itba.proyect.pedestriansim.Pedestrian;
import ar.edu.itba.proyect.pedestriansim.Scene;

public class LocationUpdater implements Updateable {

	private Scene scene;

	public LocationUpdater(Scene scene) {
		this.scene = scene;
	}

	public void update(GameContainer gc, float elapsedTimeSeconds) {
		for (Pedestrian pedestrian : scene.getPedestrians()) {
			pedestrian.updatePosition(elapsedTimeSeconds);
		}
	}

}
