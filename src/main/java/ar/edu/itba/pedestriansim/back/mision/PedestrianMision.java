package ar.edu.itba.pedestriansim.back.mision;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class PedestrianMision {

	private Map<PedestrianTarget, PedestrianTarget> _targetTransition;
	private boolean currTargetSet;
	private PedestrianTarget _current;

	public PedestrianMision() {
		_targetTransition = Maps.newHashMap();
		clear();
	}

	private PedestrianMision(Map<PedestrianTarget, PedestrianTarget> targets) {
		this();
		_targetTransition.putAll(targets);
	}

	public final void clear() {
		_targetTransition.clear();
		_current = null;
		currTargetSet = false;
	}

	public void putFirst(PedestrianTarget first) {
		putTransition(null, first);
	}

	public void putTransition(PedestrianTarget prev, PedestrianTarget next) {
		_targetTransition.put(prev, next);
	}

	public Optional<PedestrianTarget> current() {
		if (!currTargetSet) {
			_current = first();
			currTargetSet = true;
		}
		return Optional.fromNullable(_current);
	}

	private PedestrianTarget first() {
		return _targetTransition.get(null);
	}

	public PedestrianTarget next() {
		return _targetTransition.get(current());
	}

	public PedestrianTarget setNext() {
		return (_current = next());
	}

	public boolean currentIsSet() {
		return current() != null;
	}

	public boolean hasNextTarget() {
		return next() == null;
	}

	public PedestrianMision clone() {
		return new PedestrianMision(_targetTransition);
	}

}
