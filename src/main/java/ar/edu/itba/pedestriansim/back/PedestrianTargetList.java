package ar.edu.itba.pedestriansim.back;

import java.util.Map;

import com.google.common.collect.Maps;

public class PedestrianTargetList {

	private Map<PedestrianTargetArea, PedestrianTargetArea> _targets = Maps.newHashMap();

	public void putFirst(PedestrianTargetArea first) {
		put(null, first);
	}

	public void put(PedestrianTargetArea prev, PedestrianTargetArea next) {
		_targets.put(prev, next);
	}

	public PedestrianTargetArea getFirst() {
		return _targets.get(null);
	}

	public PedestrianTargetArea nextTarget(PedestrianTargetArea target) {
		if (target == null) {
			return null;
		}
		return _targets.get(target);
	}

	public boolean hasNextTarget(PedestrianTargetArea target) {
		return _targets.get(target) != null;
	}
	
}
