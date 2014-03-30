package ar.edu.itba.pedestriansim.back.component;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.physics.EulerMethod;
import ar.edu.itba.pedestriansim.back.physics.RigidBody;

public class FuturePositionUpdaterComponent extends Component {

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
		for (Pedestrian subject : _scene.getPedestrians()) {
			update(subject, elapsedTimeInSeconds);
		}
	}

	private void update(Pedestrian pedestrian, float elapsedTimeInSeconds) {
		RigidBody futureBody = pedestrian.getFuture().getBody();
		Vector2f forceOnFuture = futureBody.getAppliedForce();
		Vector2f deltaVelocity = _eulerMethod.deltaVelocity(futureBody, forceOnFuture, elapsedTimeInSeconds, velocityCache);
		Vector2f deltaPosition = _eulerMethod.deltaPosition(futureBody, forceOnFuture, elapsedTimeInSeconds, positionCache);
		futureBody.apply(deltaVelocity, deltaPosition);
		// XXX: Apply decay on future's velocity to avoid harmonic system movement! 
		futureBody.getVelocity().scale(0.9f);
	}
}
