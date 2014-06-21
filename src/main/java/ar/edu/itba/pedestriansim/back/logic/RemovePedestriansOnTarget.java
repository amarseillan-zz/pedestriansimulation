package ar.edu.itba.pedestriansim.back.logic;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

public class RemovePedestriansOnTarget extends PedestrianAreaStep {

	@Override
	public void update(PedestrianArea input) {
		List<Pedestrian> toRemove = new LinkedList<>();
		for (Pedestrian subject : input.pedestrians()) {
			if (subject.getTargetSelection().isOnFinalTarget()) {
				toRemove.add(subject);
			}
		}
		if (!toRemove.isEmpty()) {
			input.removePedestrians(toRemove);
		}
	}

}
