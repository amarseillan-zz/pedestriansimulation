package ar.edu.itba.pedestriansim.back.logic;

import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

import com.google.common.base.Function;

public abstract class PedestrianAreaStep implements Function<PedestrianArea, PedestrianArea> {

	@Override
	public PedestrianArea apply(PedestrianArea input) {
		update(input);
		return input;
	}

	public abstract void update(PedestrianArea input);

}
