package ar.edu.itba.pedestriansim.back.component;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.spatial.GridSpace;

public class GridPedestrianPositionUpdater extends Component {

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
