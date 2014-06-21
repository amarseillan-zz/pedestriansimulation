package ar.edu.itba.pedestriansim.back.entity;

import static com.google.common.base.Functions.compose;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class PedestrianSim {

	private PedestrianArea _pedestrianArea;
	private Function<PedestrianArea, PedestrianArea> _start = Functions.identity();
	private Function<PedestrianArea, PedestrianArea> _step = Functions.identity();
	private Function<PedestrianArea, PedestrianArea> _end = Functions.identity();
	private Predicate<? super PedestrianArea> _cutCondition = Predicates.alwaysTrue();
	private boolean _started = false, _finished = false;

	public PedestrianSim(PedestrianArea pedestrianArea) {
		_pedestrianArea = Preconditions.checkNotNull(pedestrianArea);
	}

	public PedestrianSim cutCondition(Predicate<? super PedestrianArea> predicate) {
		_cutCondition = predicate;
		return this;
	}

	public PedestrianSim onStart(Function<PedestrianArea, PedestrianArea> callback) {
		_start = compose(callback, _start);
		return this;
	}

	public PedestrianSim onEnd(Function<PedestrianArea, PedestrianArea> callback) {
		_end = compose(callback, _end);
		return this;
	}

	public PedestrianSim onStep(Function<PedestrianArea, PedestrianArea> callback) {
		_step = compose(callback, _step);
		return this;
	}

	public boolean isFinished() {
		return _finished;
	}

	public boolean isStarted() {
		return _started;
	}

	public void run() {
		do {
			step();
		} while (!isFinished());
	}

	public void step() {
		if (!isStarted()) {
			_started = true;
			_pedestrianArea = _start.apply(_pedestrianArea);
		}
		_pedestrianArea = _step.apply(_pedestrianArea);
		_finished = _cutCondition.apply(_pedestrianArea);
		if (isFinished()) {
			_pedestrianArea = _end.apply(_pedestrianArea);
		}
	}

	public PedestrianArea pedestrianArea() {
		return _pedestrianArea;
	}

}
