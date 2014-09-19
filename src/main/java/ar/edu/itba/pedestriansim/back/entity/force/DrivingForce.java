package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;

public class DrivingForce {

	private static final float TAU = 0.5f;
	private static final Vector2f cache = new Vector2f();

	public Vector2f getForce(RigidBody body, Vector2f target, float desiredVelocity) {
		cache.set(target);
		Vector2f e = cache.sub(body.getCenter()).normalise();
		Vector2f velocity = e.scale(desiredVelocity);
		velocity.sub(body.getVelocity());
		velocity.scale(body.getMass() / TAU);
		return velocity;
	}

}
