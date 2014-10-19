package ar.edu.itba.pedestriansim.back.logic;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.physics.EulerMethod;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;

import com.google.common.base.Optional;

public class FuturePositionUpdaterComponent extends PedestrianAreaStep {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FuturePositionUpdaterComponent.class);

	private final Vector2f velocityCache = new Vector2f();
	private final Vector2f positionCache = new Vector2f();

	private EulerMethod _eulerMethod = new EulerMethod();

	private Optional<? extends RandomGenerator> _longitudinalNoise, _perpendicularNoise;

	public FuturePositionUpdaterComponent(Optional<? extends RandomGenerator> longitudinalNoise, Optional<? extends RandomGenerator> perpendicularNoise) {
		_longitudinalNoise = longitudinalNoise;
		_perpendicularNoise = perpendicularNoise;
	}

	@Override
	public void update(PedestrianArea input) {
		for (Pedestrian subject : input.pedestrians()) {
			update(input, subject);
		}
	}

	private void update(PedestrianArea input, Pedestrian pedestrian) {
		RigidBody futureBody = pedestrian.getFuture().getBody();
		Vector2f forceOnFuture = addNoise(futureBody.getAppliedForce());
		float elapsedTimeInSeconds = input.timeStep().floatValue();
		Vector2f deltaVelocity = _eulerMethod.deltaVelocity(futureBody, forceOnFuture, elapsedTimeInSeconds, velocityCache);
		Vector2f deltaPosition = _eulerMethod.deltaPosition(futureBody, forceOnFuture, elapsedTimeInSeconds, positionCache);
		futureBody.apply(deltaVelocity, deltaPosition);
	}

	private Vector2f addNoise(Vector2f vector) {
		if (!_longitudinalNoise.isPresent() && !_perpendicularNoise.isPresent()) {
			return vector;
		}
		Vector2f result = new Vector2f();
		if (_perpendicularNoise.isPresent()) {
			float sign = Math.signum((float) Math.random() - 0.5f);
			result.add(vector.copy().add(sign * 90).scale(_perpendicularNoise.get().generate()));
		}
		if (_longitudinalNoise.isPresent()) {
			result.add(vector.copy().scale(_longitudinalNoise.get().generate()));
		}
		return result.add(vector);
	}
}
