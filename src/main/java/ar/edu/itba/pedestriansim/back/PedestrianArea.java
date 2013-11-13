package ar.edu.itba.pedestriansim.back;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.geom.Shape;

public class PedestrianArea {

	private final List<Pedestrian> _pedestrians = new ArrayList<>();
	private final List<Shape> _obstacles = new ArrayList<>();
	private final List<PedestrianSource> _sources = new ArrayList<>();

	public void addPedestrian(Pedestrian pedestrian) {
		_pedestrians.add(pedestrian);
	}

	public void addObstacle(Shape obstacte) {
		_obstacles.add(obstacte);
	}

	public void addSource(PedestrianSource source) {
		source.start();
		_sources.add(source);
	}

	public Collection<Shape> getObstacles() {
		return _obstacles;
	}

	public Collection<Pedestrian> getPedestrians() {
		return _pedestrians;
	}

	public Collection<PedestrianSource> getSources() {
		return _sources;
	}
	
	public void removePedestrians(Collection<Pedestrian> pedestrians) {
		_pedestrians.removeAll(pedestrians);
	}
	
	public boolean collides(Shape shape) {
		for (Pedestrian pedestrian : _pedestrians) {
			Shape pedestrianShape = pedestrian.getBody().getCollitionShape().getShape();
			if (Collitions.touching(pedestrianShape, shape)) {
				return false;
			}
		}
		// XXX: marse, acordate que me dijsite que no iban a chocar contra las paredes...
		return true;
	}
}
