package ar.edu.itba.pedestriansim.back;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;


public class Scene implements Updateable {

	private List<Shape> obstacles = new ArrayList<Shape>();
	private List<Pedestrian> pedestrians = new ArrayList<Pedestrian>();

	public Scene() {
	}

	public void addObstacle(Shape shape) {
		obstacles.add(shape);
	}

	public void addPedestrian(Pedestrian pedestrian) {
		pedestrians.add(pedestrian);
	}

	public List<Shape> getObstacles() {
		return Collections.unmodifiableList(obstacles);
	}

	public List<Pedestrian> getPedestrians() {
		return Collections.unmodifiableList(pedestrians);
	}

	public void update(GameContainer gc, float elapsedTimeSeconds) {
		
	}
}
