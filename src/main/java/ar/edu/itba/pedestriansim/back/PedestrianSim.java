package ar.edu.itba.pedestriansim.back;

import java.util.List;

import ar.edu.itba.common.event.EventDispatcher;
import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.factory.ComponentFactory;
import ar.edu.itba.pedestriansim.factory.PedestrianAreaFactory;
import ar.edu.itba.pedestriansim.gui.Camera;

public class PedestrianSim implements Updateable {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();

	private PedestrianArea _pedestrianArea;
	private List<Updateable> _components;

	public PedestrianSim(PedestrianAppConfig config, Camera camera) {
		_pedestrianArea = new PedestrianAreaFactory(config).produce(camera);
		_components = new ComponentFactory(config).produce(_pedestrianArea);
	}
	
	public void update(float elapsedTimeInSeconds) {
		dispatcher.update(elapsedTimeInSeconds);
		for (Updateable component : _components) {
			component.update(elapsedTimeInSeconds);
		}
	}

	public PedestrianArea getPedestrianArea() {
		return _pedestrianArea;
	}

}
