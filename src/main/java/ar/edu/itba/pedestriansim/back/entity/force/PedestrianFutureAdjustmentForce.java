package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianFuture;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

import com.google.common.base.Preconditions;

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
		return new Vector2f()
			.add(springAlignmentForce(input))
			.add(springSeparationPosition(input));
	}

	private Vector2f springAlignmentForce(Pedestrian input) {
		PedestrianFuture future = input.getFuture();
		Vector2f position = input.getBody().getCenter();
		Vector2f target = input.getTargetSelection().getTarget().getClosesPoint(position);
		return Vectors.pointBetween(position, target, input.getReactionDistance(), positionCache)
			.sub(future.getBody().getCenter())
			.scale(_kAlign)
			.add(new Vector2f(future.getBody().getVelocity()).scale(-_sigma));
	}

	private Vector2f springSeparationPosition(Pedestrian input) {
		Vector2f pedestrianCenter = input.getBody().getCenter();
		Vector2f pedestrianFutureCenter = input.getFuture().getBody().getCenter();
		Vector2f target = input.getTargetSelection().getTarget().getClosesPoint(pedestrianCenter); 
		float deviationAngle = angle(pedestrianCenter, target, pedestrianFutureCenter);
		float angleDeviationDecay = 1;
		if (deviationAngle > 1) {
			// XXX: careful with 0 division here!! 
			angleDeviationDecay /= deviationAngle * 10;
		}
		return 
			Vectors.pointBetween(pedestrianCenter, pedestrianFutureCenter, input.getReactionDistance(), positionCache)
			.sub(pedestrianFutureCenter)
			.normalise()
			.scale(_kSeparation * angleDeviationDecay);
	}

	private float angle(Vector2f center, Vector2f target, Vector2f future) {
		Vector2f d1 = future.copy().sub(center);
		Vector2f d2 = target.copy().sub(center);
		float angle = (float) Math.abs(d1.getTheta() - d2.getTheta());
		if (angle > 180) {
			angle = 360 - angle;
		}
		Preconditions.checkArgument(0 <= angle && angle <= 180);
		return angle;
	}
}
