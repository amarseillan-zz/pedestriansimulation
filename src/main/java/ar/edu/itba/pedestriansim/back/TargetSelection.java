package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.mision.GoToSelfLocation;
import ar.edu.itba.pedestriansim.back.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.mision.PedestrianTarget;
import ar.edu.itba.pedestriansim.physics.RigidBody;

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

	public float getETA() {
		float velocity = getBody().getVelocity().length();
		if (Math.abs(velocity) < 0.01) {
			return Float.NaN;
		}
		return getTarget().distanceTo(getBody().getCenter()) / velocity;
	}

	private PedestrianMision getMission() {
		return _pedestrian.getMission();
	}

	private RigidBody getBody() {
		return _pedestrian.getBody();
	}

	private Shape getShape() {
		return _pedestrian.getShape();
	}
}
