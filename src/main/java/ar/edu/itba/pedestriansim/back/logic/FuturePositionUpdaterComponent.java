package ar.edu.itba.pedestriansim.back.logic;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
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
		if (inBounds(input, pedestrian, deltaPosition)) {
			futureBody.apply(deltaVelocity, deltaPosition);
		}
	}
	
	private boolean inBounds(PedestrianArea input, Pedestrian pedestrian, Vector2f deltaPosition) {
		RigidBody future = pedestrian.getFuture().getBody();
		Vector2f actual = future.getCenter();
		Vector2f next = actual.copy().add(deltaPosition);
		Line cross = new Line(actual, next);
		for (Shape shape : input.obstacles()) {
			if (!(shape instanceof Line)) {
				logger.error("obstacles that are not lines are not yet supported");
				throw new RuntimeException();
			}
			Line line = (Line) shape;
			if (cross.intersects(line)) {
				return false;
			}
		}
		
		return true;
	}
}
