package ar.edu.itba.pedestriansim.back.logic;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.physics.EulerMethod;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class PedestrianPositionUpdaterComponent extends PedestrianAreaStep {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PedestrianPositionUpdaterComponent.class);

	private final Vector2f velocityCache = new Vector2f();
	private final Vector2f positionCache = new Vector2f();

	private EulerMethod _eulerMethod = new EulerMethod();

	@Override
	public void update(PedestrianArea input) {
		for (Pedestrian pedestrian : input.pedestrians()) {
			updateRigidBody(pedestrian.getBody(), input.timeStep().floatValue());
			pedestrian.getTargetSelection().updateTarget();
			pedestrian.getBody().setAppliedForce(Vectors.nullVector());
		}
	}

	private void updateRigidBody(RigidBody body, float elapsedTimeInSeconds) {
		Vector2f force = body.getAppliedForce();
		Vector2f deltaVelocity = _eulerMethod.deltaVelocity(body, force, elapsedTimeInSeconds, velocityCache);
		Vector2f deltaPosition = _eulerMethod.deltaPosition(body, force, elapsedTimeInSeconds, positionCache);
		body.apply(deltaVelocity, deltaPosition);
	}

}
