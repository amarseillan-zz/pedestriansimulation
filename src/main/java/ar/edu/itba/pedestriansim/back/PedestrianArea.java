package ar.edu.itba.pedestriansim.back;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.geom.Shape;

public class PedestrianArea {

	private List<Pedestrian> _pedestrians = new ArrayList<Pedestrian>();
	private List<Shape> _obstacles = new ArrayList<Shape>();
	
	public void addPedestrian(Pedestrian pedestrian) {
		_pedestrians.add(pedestrian);
	}

	public void addObstacle(Shape obstacte) {
		_obstacles.add(obstacte);
	}

	public Collection<Shape> getObstacles() {
		return _obstacles;
	}

	public Collection<Pedestrian> getPedestrians() {
		return _pedestrians;
	}

}
