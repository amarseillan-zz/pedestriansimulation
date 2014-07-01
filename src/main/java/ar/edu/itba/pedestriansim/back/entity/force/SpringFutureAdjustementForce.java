package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class SpringFutureAdjustementForce implements PedestrianForce {

	private final float _K = 100;
	private Vector2f forceOnFuture = new Vector2f();

	@Override
	public Vector2f apply(Pedestrian input) {
		Vector2f position = input.getBody().getCenter();
		Vector2f target = input.getTargetSelection().getTarget().getClosesPoint(position);
		Vectors.pointBetween(position, target, input.getReactionDistance(), forceOnFuture);
		Vector2f futurePosition = input.getFuture().getBody().getCenter();
		forceOnFuture.sub(futurePosition);
		if (forceOnFuture.lengthSquared() < 0.01) {
			// Objective is close enough, no need to apply forces
			return Vectors.nullVector();
		}
		return forceOnFuture.normalise().scale(_K);
	}
}
