package ar.edu.itba.pedestriansim.back.physics;

import org.newdawn.slick.geom.Vector2f;

public class DrivingForce {

	private static final float TAU = 0.5f;
	private static final Vector2f cache = new Vector2f();

	public Vector2f getForce(RigidBody body, Vector2f target, float desiredVelocity) {
		cache.set(target);
		Vector2f forceDir = cache.sub(body.getCenter()).normalise();
		Vector2f velocity = forceDir.scale(desiredVelocity);
		velocity.sub(body.getVelocity());
		return velocity.scale(body.getMass() / TAU);
	}

}
