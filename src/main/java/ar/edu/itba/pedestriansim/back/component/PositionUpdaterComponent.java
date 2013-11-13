package ar.edu.itba.pedestriansim.back.component;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.EulerMethod;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.RigidBody;
import ar.edu.itba.pedestriansim.back.Updateable;

public class PositionUpdaterComponent implements Updateable {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PositionUpdaterComponent.class);

	private final Vector2f velocityCache = new Vector2f();
	private final Vector2f positionCache = new Vector2f();

	private EulerMethod _eulerMethod;
	private PedestrianArea _scene;

	public PositionUpdaterComponent(PedestrianArea scene) {
		_scene = scene;
		_eulerMethod = new EulerMethod();
	}

	public void update(float elapsedTimeInSeconds) {
		for (Pedestrian pedestrian : _scene.getPedestrians()) {
			RigidBody body = pedestrian.getFuture().getBody();
			Vector2f force = body.getAppliedForce();
			Vector2f deltaVelocity = _eulerMethod.deltaVelocity(body, force, elapsedTimeInSeconds, velocityCache);
			Vector2f deltaPosition = _eulerMethod.deltaPosition(body, force, elapsedTimeInSeconds, positionCache);
			body.apply(deltaVelocity, deltaPosition);
			pedestrian.updateTarget();
		}
	}

}
