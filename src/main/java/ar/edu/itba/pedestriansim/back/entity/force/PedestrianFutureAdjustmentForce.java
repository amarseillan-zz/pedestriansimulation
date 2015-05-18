package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianFuture;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class PedestrianFutureAdjustmentForce implements PedestrianForce {

	private final float _kAlign;
	private final float _sigma;
	private final float _kSeparation;
	private final Vector2f positionCache = new Vector2f();

	public PedestrianFutureAdjustmentForce(float kAlign, float sigma, float kSeparation) {
		_kAlign = kAlign;
		_sigma = sigma;
		_kSeparation = kSeparation;
	}

	@Override
	public Vector2f apply(Pedestrian input) {
		Vector2f position = input.getBody().getCenter();
		Vector2f target = input.getTargetSelection().getTarget().getClosesPoint(position);
		return new Vector2f()
			.add(springAlignmentForce(input, target))
			.add(springSeparationPosition(input, target));
	}

	private Vector2f springAlignmentForce(Pedestrian input, Vector2f target) {
		PedestrianFuture future = input.getFuture();
		Vector2f position = input.getBody().getCenter();
		float targetDist = target.distance(input.getBody().getCenter());
		float c = targetDist > 1.5f ? 1 : (1 + (2f * (1.5f - targetDist) / 1.5f));
		return Vectors.pointBetween(position, target, input.getReactionDistance(), positionCache)
			.sub(future.getBody().getCenter())
			.scale(_kAlign * c)
			.add(new Vector2f(future.getBody().getVelocity()).scale(-_sigma));
	}

	private Vector2f springSeparationPosition(Pedestrian input, Vector2f target) {
		Vector2f pedestrianFutureCenter = input.getFuture().getBody().getCenter();
		Vector2f position = input.getBody().getCenter();
		float deviationAngle = angle(position, target, pedestrianFutureCenter);
		float angleDeviationDecay = 1;
		if (deviationAngle > 31) {
			angleDeviationDecay /= (deviationAngle - 30);
		}
//		float targetDist = target.distance(input.getBody().getCenter());
//		float c = targetDist > 2f ? 1 : (1 + (2f * (2f - targetDist) / 2f));
		return 
			Vectors.pointBetween(position, pedestrianFutureCenter, input.getReactionDistance(), positionCache)
			.sub(pedestrianFutureCenter)
			.normalise()
			.scale(_kSeparation * angleDeviationDecay);
	}

	private float angle(Vector2f center, Vector2f target, Vector2f future) {
		return Vectors.angle(future.copy().sub(center), target.copy().sub(center));
	}
}
