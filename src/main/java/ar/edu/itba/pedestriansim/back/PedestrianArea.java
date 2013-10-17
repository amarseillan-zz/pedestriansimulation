package ar.edu.itba.pedestriansim.back;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.geom.Shape;

public class PedestrianArea {

	private List<Pedestrian> pedestrians = new ArrayList<Pedestrian>();

	public void addPedestrian(Pedestrian pedestrian) {
		pedestrians.add(pedestrian);
	}

	public List<Shape> getObstacles() {
		return Collections.emptyList();
	}

	public List<Pedestrian> getPedestrians() {
		return Collections.unmodifiableList(pedestrians);
	}

}
