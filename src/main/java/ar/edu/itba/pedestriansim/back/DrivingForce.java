package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class DrivingForce {

	private static final float TAU = 0.5f;

	public Vector2f getForce(RigidBody body, Vector2f target, float desiredVelocity) {
		Vector2f forceDir = new Vector2f(target).sub(body.getCenter()).normalise();		
		Vector2f velocity = forceDir.scale(desiredVelocity);
		velocity.sub(body.getVelocity());
		return velocity.scale(body.getMass() / TAU);
	}
}
