package ar.edu.itba.pedestriansim.back;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

public class PedestrianMovementStrategy {

	private List<Vector2f> _pendingTargets = new LinkedList<>();
	private Vector2f _current;

	public PedestrianMovementStrategy() {
		this(new LinkedList<Vector2f>());
	}

	public PedestrianMovementStrategy(Vector2f target) {
		this(Collections.singletonList(target));
	}

	public PedestrianMovementStrategy(Collection<Vector2f> pendingTargets) {
		_pendingTargets.addAll(pendingTargets);
		nextTarget();
	}

	public void append(Vector2f target) {
		_pendingTargets.add(target);
		if (_current == null) {
			nextTarget();
		}
	}

	public Vector2f nextTarget() {
		if (!_pendingTargets.isEmpty()) {
			_current = _pendingTargets.remove(0);
		}
		return currentTarget();
	}

	public Vector2f currentTarget() {
		return _current;
	}

	public boolean hasNextTarget() {
		return !_pendingTargets.isEmpty();
	}

	public PedestrianMovementStrategy copy() {
		PedestrianMovementStrategy copy = new PedestrianMovementStrategy();
		copy._pendingTargets.addAll(_pendingTargets);
		copy._current = _current;
		return copy;
	}
}
