package ar.edu.itba.pedestriansim.back.component;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.physics.EulerMethod;
import ar.edu.itba.pedestriansim.physics.RigidBody;

public class FuturePositionUpdaterComponent implements Updateable {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FuturePositionUpdaterComponent.class);

	private final Vector2f velocityCache = new Vector2f();
	private final Vector2f positionCache = new Vector2f();

	private EulerMethod _eulerMethod;
	private PedestrianArea _scene;

	public FuturePositionUpdaterComponent(PedestrianArea scene) {
		_scene = scene;
		_eulerMethod = new EulerMethod();
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian pedestrian : _scene.getPedestrians()) {
			updateRigidBody(pedestrian.getFuture().getBody(), elapsedTimeInSeconds);
		}
	}

	private void updateRigidBody(RigidBody body, float elapsedTimeInSeconds) {
		Vector2f force = body.getAppliedForce();
		Vector2f deltaVelocity = _eulerMethod.deltaVelocity(body, force, elapsedTimeInSeconds, velocityCache);
		Vector2f deltaPosition = _eulerMethod.deltaPosition(body, force, elapsedTimeInSeconds, positionCache);
		body.apply(deltaVelocity, deltaPosition);
	}

}
