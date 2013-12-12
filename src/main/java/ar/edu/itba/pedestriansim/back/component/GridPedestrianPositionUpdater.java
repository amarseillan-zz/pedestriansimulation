package ar.edu.itba.pedestriansim.back.component;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.spatial.GridSpace;

public class GridPedestrianPositionUpdater implements Updateable {

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
