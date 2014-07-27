package ar.edu.itba.pedestriansim.back.logic;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.physics.EulerMethod;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;

public class FuturePositionUpdaterComponent extends PedestrianAreaStep {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FuturePositionUpdaterComponent.class);

	private final Vector2f velocityCache = new Vector2f();
	private final Vector2f positionCache = new Vector2f();

	private EulerMethod _eulerMethod = new EulerMethod();

	@Override
	public void update(PedestrianArea input) {
		for (Pedestrian subject : input.pedestrians()) {
			update(input, subject);
		}
	}

	private void update(PedestrianArea input, Pedestrian pedestrian) {
		RigidBody futureBody = pedestrian.getFuture().getBody();
		Vector2f forceOnFuture = futureBody.getAppliedForce();
		float elapsedTimeInSeconds = input.timeStep().floatValue();
		Vector2f deltaVelocity = _eulerMethod.deltaVelocity(futureBody, forceOnFuture, elapsedTimeInSeconds, velocityCache);
		Vector2f deltaPosition = _eulerMethod.deltaPosition(futureBody, forceOnFuture, elapsedTimeInSeconds, positionCache);
		futureBody.apply(deltaVelocity, deltaPosition);
		// XXX: Apply decay on future's velocity to avoid harmonic system
		// movement!
		futureBody.getVelocity().scale(1-(elapsedTimeInSeconds*10));
	}
}
