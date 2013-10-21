package ar.edu.itba.pedestriansim.back;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.pedestriansim.back.component.ForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianRemoverComponent;
import ar.edu.itba.pedestriansim.back.component.PositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.event.EventDispatcher;

public class PedestrianSim implements Updateable {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();
	private final PedestrianArea _scene;
	private final List<Updateable> _components = new LinkedList<Updateable>();

	public PedestrianSim() {
		_scene = new PedestrianArea();
		_components.add(new ForceUpdaterComponent(_scene));
		_components.add(new PositionUpdaterComponent(_scene));
		_components.add(new PedestrianRemoverComponent(_scene));
	}

	public void update(float elapsedTimeInSeconds) {
		dispatcher.update(elapsedTimeInSeconds);
		for (Updateable updatable : _components) {
			updatable.update(elapsedTimeInSeconds);
		}
	}

	public PedestrianArea getScene() {
		return _scene;
	}

}
