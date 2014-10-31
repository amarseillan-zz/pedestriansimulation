package ar.edu.itba.pedestriansim.back.entity.physics;

import org.newdawn.slick.geom.Vector2f;

public interface IntegrationFunction {

	Vector2f deltaVelocity(RigidBody body, Vector2f force, float dtInSeconds, Vector2f result);

	Vector2f deltaPosition(RigidBody body, Vector2f force, float dtInSeconds, Vector2f result);

}
