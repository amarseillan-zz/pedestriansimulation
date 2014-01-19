package ar.edu.itba.pedestriansim.back.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;

public class ConstantValueForce implements PedestrianForce {

	@Override
	public Vector2f apply(Pedestrian input) {
		Vector2f target = input.getTargetSelection().getTarget().getCenter();
		Vector2f future = input.getFuture().getBody().getCenter();
		return target.copy().sub(future).normalise().scale(500);
	}

}
