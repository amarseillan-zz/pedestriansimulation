package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class EulerMethod {

	public Vector2f acceleration(RigidBody body, Vector2f force) {
		return force.copy().scale(1 / body.getMass());
	}

	public Vector2f deltaVelocity(RigidBody body, Vector2f force, float dtInSeconds) {
		return acceleration(body, force).scale(dtInSeconds); 
	}

	public Vector2f deltaPosition(RigidBody body, Vector2f force, float dtInSeconds) {
		Vector2f dx1 = acceleration(body, force).scale(dtInSeconds * dtInSeconds / 2);
		return body.getVelocity().copy().scale(dtInSeconds).add(dx1);
	}

}
