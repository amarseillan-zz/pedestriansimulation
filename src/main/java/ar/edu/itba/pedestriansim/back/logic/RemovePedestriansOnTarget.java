package ar.edu.itba.pedestriansim.back.logic;

import java.util.List;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

import com.google.common.collect.Lists;

public class RemovePedestriansOnTarget extends PedestrianAreaStep {

	@Override
	public void update(PedestrianArea input) {
		List<Pedestrian> toRemove = Lists.newLinkedList();
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
