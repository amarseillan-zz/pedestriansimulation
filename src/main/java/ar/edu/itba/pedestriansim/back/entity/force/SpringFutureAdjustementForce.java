package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianFuture;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class SpringFutureAdjustementForce implements PedestrianForce {

	private final float _K;
	private final float _sigma;
	private final Vector2f desiredFuturePosition = new Vector2f();

	public SpringFutureAdjustementForce(float K, float sigma) {
		_K = K;
		_sigma = sigma;
	}

	@Override
	public Vector2f apply(Pedestrian input) {
		Vector2f desiredFutureLocation = desireFuturePosition(input);
		PedestrianFuture future = input.getFuture();
		desiredFutureLocation.sub(future.getBody().getCenter());
		desiredFutureLocation.scale(_K);
		desiredFutureLocation.add(new Vector2f(future.getBody().getVelocity()).scale(-_sigma));
		return desiredFutureLocation;
	}

	private Vector2f desireFuturePosition(Pedestrian input) {
		Vector2f position = input.getBody().getCenter();
		Vector2f target = input.getTargetSelection().getTarget().getClosesPoint(position);
		return Vectors.pointBetween(position, target, input.getReactionDistance(), desiredFuturePosition);
	}
}
