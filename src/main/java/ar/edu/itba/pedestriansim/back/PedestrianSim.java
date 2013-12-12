package ar.edu.itba.pedestriansim.back;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.pedestriansim.back.component.FutureForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.GridPedestrianPositionUpdater;
import ar.edu.itba.pedestriansim.back.component.PedestrianForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianPositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianRemoverComponent;
import ar.edu.itba.pedestriansim.back.event.EventDispatcher;
import ar.edu.itba.pedestriansim.back.replusionforce.FutureRepulsionForce1;
import ar.edu.itba.pedestriansim.back.replusionforce.RepulsionForce;

public class PedestrianSim implements Updateable {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();
	private final PedestrianArea _scene;
	private final List<Updateable> _components = new LinkedList<Updateable>();

	public PedestrianSim(int width, int height, int gridSize) {
		_scene = new PedestrianArea(width, height, gridSize);
		RepulsionForce repulsionForce = new FutureRepulsionForce1(40, 10);
		_components.add(new FutureForceUpdaterComponent(_scene, Pedestrians.getFutureLocation(), 5, repulsionForce));
		_components.add(new FuturePositionUpdaterComponent(_scene));
		_components.add(new PedestrianForceUpdaterComponent(_scene));
		_components.add(new PedestrianPositionUpdaterComponent(_scene));
		_components.add(new PedestrianRemoverComponent(_scene));
		_components.add(new GridPedestrianPositionUpdater(_scene));
	}

	public void update(float elapsedTimeInSeconds) {
		dispatcher.update(elapsedTimeInSeconds);
		for (Updateable component : _components) {
			component.update(elapsedTimeInSeconds);
		}
	}

	public PedestrianArea getScene() {
		return _scene;
	}

}
