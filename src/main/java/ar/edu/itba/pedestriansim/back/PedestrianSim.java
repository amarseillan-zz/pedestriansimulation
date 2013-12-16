package ar.edu.itba.pedestriansim.back;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.pedestriansim.back.event.EventDispatcher;
import ar.edu.itba.pedestriansim.creator.PedestrianAreaCreator;
import ar.edu.itba.pedestriansim.creator.UpdateListCreator;
import ar.edu.itba.pedestriansim.gui.Camera;

@Component
public class PedestrianSim implements Updateable {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();

	@Autowired
	private PedestrianAreaCreator pedestrianAreaCreator;

	@Autowired
	private UpdateListCreator updateListCreator;

	private PedestrianArea _pedestrianArea;
	private List<Updateable> _components;

	public void init(Camera camera) {
		_pedestrianArea = pedestrianAreaCreator.produce(camera);
		_components = updateListCreator.produce(_pedestrianArea);
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
