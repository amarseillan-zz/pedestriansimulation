package ar.edu.itba.pedestriansim.back.entity;

import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.entity.mision.GoToSelfLocation;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianTarget;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class TargetSelection {

	private final Pedestrian _pedestrian;
	private PedestrianTarget goToSelf;

	public TargetSelection(Pedestrian pedestrian) {
		_pedestrian = Preconditions.checkNotNull(pedestrian);
	}

	public PedestrianTarget getTarget() {
		Optional<PedestrianTarget> current = getOptionalTarget();
		PedestrianTarget target = null;
		if (current.isPresent()) {
			target = current.get();
		} else {
			if (goToSelf == null) {
				goToSelf = new GoToSelfLocation(_pedestrian);
			}
			target = goToSelf;
		}
		return target;
	}

	private Optional<PedestrianTarget> getOptionalTarget() {
		if (getMission() == null) {
			return Optional.absent();
		}
		return getMission().current();
	}

	public boolean isOnTarget() {
		Optional<PedestrianTarget> target = getOptionalTarget(); 
		return !target.isPresent() || target.get().intersects(getShape());
	}

	public boolean isOnFinalTarget() {
		return isOnTarget() && !getMission().hasNextTarget();
	}

	public void updateTarget() {
		if (isOnTarget()) {
			_pedestrian.stop();
			getMission().setNext();
		}
	}

	private PedestrianMision getMission() {
		return _pedestrian.getMission();
	}

	private Shape getShape() {
		return _pedestrian.getShape();
	}
}
