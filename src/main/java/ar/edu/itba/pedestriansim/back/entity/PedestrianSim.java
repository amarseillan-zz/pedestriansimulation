package ar.edu.itba.pedestriansim.back.entity;

import java.util.List;

import ar.edu.itba.pedestriansim.back.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.component.Component;
import ar.edu.itba.pedestriansim.back.event.EventDispatcher;
import ar.edu.itba.pedestriansim.back.factory.PedestrianAreaFactory;
import ar.edu.itba.pedestriansim.back.factory.SimulationComponentsFactory;

import com.google.common.base.Predicate;

public class PedestrianSim {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();

	private PedestrianArea _pedestrianArea;
	private List<Component> _components;
	private Predicate<PedestrianArea> _cutCondition;
	private boolean _finished;

	public PedestrianSim(PedestrianAppConfig config, SimulationComponentsFactory components, Predicate<PedestrianArea> cutCondition) {
		_pedestrianArea = new PedestrianAreaFactory(config).produce();
		_components = components.produce(config, _pedestrianArea);
		_cutCondition = cutCondition;
	}

	public void start() {
		_pedestrianArea.init();
		for (Component component : _components) {
			component.onStart();
		}
	}

	public void update(float elapsedTimeInSeconds) {
		if (isFinished()) {
			throw new IllegalStateException("Already finished!");
		}
		dispatcher.update(elapsedTimeInSeconds);
		for (Updateable component : _components) {
			component.update(elapsedTimeInSeconds);
		}
		_pedestrianArea.addElapsedTime(elapsedTimeInSeconds);
		_finished = _cutCondition.apply(_pedestrianArea);
		if (isFinished()) {
			end();
		}
	}
	
	public boolean isFinished() {
		return _finished;
	}

	public void end() {
		for (Component component : _components) {
			component.onEnd();
		}
	}

	public PedestrianArea getPedestrianArea() {
		return _pedestrianArea;
	}

}
