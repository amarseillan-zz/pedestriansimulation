package ar.edu.itba.pedestriansim.back.entity.physics;

import org.newdawn.slick.geom.Vector2f;

public class EulerMethod implements IntegrationFunction {

	private Vector2f acceleration(RigidBody body, Vector2f force, Vector2f result) {
		result.set(force);
		return result.scale(1 / body.getMass());
	}

	@Override
	public Vector2f deltaVelocity(RigidBody body, Vector2f force, float dtInSeconds, Vector2f result) {
		return acceleration(body, force, result).scale(dtInSeconds);
	}

	@Override
	public Vector2f deltaPosition(RigidBody body, Vector2f force, float dtInSeconds, Vector2f result) {
		Vector2f dx1 = acceleration(body, force, result).scale(dtInSeconds * dtInSeconds / 2);
		return body.getVelocity().copy().scale(dtInSeconds).add(dx1);
	}

}
