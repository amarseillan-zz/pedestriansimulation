package ar.edu.itba.pedestriansim.back.component;

import ar.edu.itba.common.spatial.GridSpace;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;

public class GridPedestrianPositionUpdater extends Componenent {

	private PedestrianArea _pedestrianArea;

	public GridPedestrianPositionUpdater(PedestrianArea pedestrianArea) {
		_pedestrianArea = pedestrianArea;
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		GridSpace<Pedestrian> map = _pedestrianArea.getMap();
		map.clear();
		for (Pedestrian pedestrian : _pedestrianArea.getPedestrians()) {
			map.add(pedestrian);
		}
	}

}
