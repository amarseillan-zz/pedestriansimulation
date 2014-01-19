package ar.edu.itba.pedestriansim.back.component;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.physics.EulerMethod;
import ar.edu.itba.pedestriansim.physics.RigidBody;
import ar.edu.itba.pedestriansim.physics.Vectors;

public class FuturePositionUpdaterComponent implements Updateable {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FuturePositionUpdaterComponent.class);
	private static final Vectors vectors = new Vectors();

	private final Vector2f futureLocation = new Vector2f();
//	private final Vector2f velocityCache = new Vector2f();
	private final Vector2f positionCache = new Vector2f();
	private final Vector2f sideLocationCache = new Vector2f();

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
		// TODO: No need for velocity in case of future?
//		Vector2f deltaVelocity = _eulerMethod.deltaVelocity(futureBody, forceOnFuture, elapsedTimeInSeconds, velocityCache);
		Vector2f deltaPosition = _eulerMethod.deltaPosition(futureBody, forceOnFuture, elapsedTimeInSeconds, positionCache);
		// XXX: Pedestrian future is rotated only!!
		Vector2f transformed = translateAsRotation(deltaPosition, pedestrian);
		pedestrian.getFuture().getBody().setLocation(transformed);
		ensureCorrectDistanceToFuture(pedestrian);
	}

	private Vector2f translateAsRotation(Vector2f deltaPosition, Pedestrian pedestrian) {
		float theeta = deltaPosition.length() / pedestrian.getReactionDistance();
		Vector2f futureCenter = pedestrian.getFuture().getBody().getCenter();
		Vector2f pedestrianCenter = pedestrian.getBody().getCenter();
		sideLocationCache.set(futureCenter);
		sideLocationCache.add(positionCache);
		boolean targetSide = Vectors.isLeft(pedestrianCenter, futureCenter, sideLocationCache);
		theeta *= (targetSide ? 1 : -1);
		return Transform.createRotateTransform(theeta, pedestrianCenter.x, pedestrianCenter.y).transform(futureCenter);
	}
	
	private void ensureCorrectDistanceToFuture(Pedestrian pedestrian) {
		Vector2f pedestrianCenter = pedestrian.getBody().getCenter();
		Vector2f futureCenter = pedestrian.getFuture().getBody().getCenter();
		vectors.pointBetween(pedestrianCenter, futureCenter, pedestrian.getReactionDistance(), futureLocation);
		pedestrian.getFuture().getBody().setLocation(futureLocation);
	}

}
