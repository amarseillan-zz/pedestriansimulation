package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;

import com.google.common.base.Preconditions;

public class ReactionDistanceDesireForce implements PedestrianForce {

	public static enum FunctionType {
		LINEAR {
			@Override
			float p(float distance, float maxDistance) {
				distance = Math.min(distance, maxDistance);
				return distance / maxDistance;
			}
		},
		SQ {
			@Override
			float p(float distance, float maxDistance) {
				float p = LINEAR.p(distance, maxDistance);
				return p * p;
			}
		},
		SQRT {
			@Override
			float p(float distance, float maxDistance) {
				float p = LINEAR.p(distance, maxDistance);
				return (float) Math.sqrt(p);
			}
		};
		abstract float p(float distance, float maxDistance);
	};

	private final FunctionType _functionType;
	private final DrivingForce _forceModel;

	public ReactionDistanceDesireForce(FunctionType functionType) {
		_forceModel = new DrivingForce();
		_functionType = functionType;

	}

	@Override
	public Vector2f apply(Pedestrian subject) {
		Vector2f target = subject.getFuture().getBody().getCenter();
		float distance = subject.getBody().getCenter().distance(target);
		float p = _functionType.p(distance, subject.getReactionDistance());
		Preconditions.checkArgument(0 <= p && p <= 1);
		return _forceModel.getForce(subject.getBody(), target, subject.getMaxVelocity() * p);

	}
}
