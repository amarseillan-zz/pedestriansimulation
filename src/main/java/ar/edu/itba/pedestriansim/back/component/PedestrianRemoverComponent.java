package ar.edu.itba.pedestriansim.back.component;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

public class PedestrianRemoverComponent extends Component {

	private final PedestrianArea _scene;
	
	public PedestrianRemoverComponent(PedestrianArea scene) {
		_scene = scene;
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		List<Pedestrian> toRemove = new LinkedList<>();
		for (Pedestrian subject : _scene.getPedestrians()) {
			if (subject.getTargetSelection().isOnFinalTarget()) {
				toRemove.add(subject);
			}
		}
		if (!toRemove.isEmpty()) {
			_scene.removePedestrians(toRemove);
		}
	}
	
}
