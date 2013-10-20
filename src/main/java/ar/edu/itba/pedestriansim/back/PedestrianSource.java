package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class PedestrianSource {

	private int lastId = 0;
	private Vector2f location;
	private int mass = 50;
	private Vector2f target;

	public void setTarget(Vector2f target) {
		this.target = target;
	}

	public Pedestrian produce() {
		return new Pedestrian(lastId++, mass, location, target.copy());
	}

}
