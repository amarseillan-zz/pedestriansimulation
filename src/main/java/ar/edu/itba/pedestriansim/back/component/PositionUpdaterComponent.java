package ar.edu.itba.pedestriansim.back.component;


import org.apache.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.EulerForce;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.RigidBody;
import ar.edu.itba.pedestriansim.back.Updateable;

public class PositionUpdaterComponent implements Updateable {

	private static final Logger logger = Logger.getLogger(PositionUpdaterComponent.class);
	
	private EulerForce _eulerMethod;
	private PedestrianArea _scene;

	public PositionUpdaterComponent(PedestrianArea scene) {
		_scene = scene;
		_eulerMethod = new EulerForce();
	}

	public void update(GameContainer gc, float elapsedTimeInSeconds) {
		for (Pedestrian pedestrian : _scene.getPedestrians()) {
			RigidBody body = pedestrian.getBody();
			Vector2f force = pedestrian.getAppliedForce();
			Vector2f deltaVelocity = _eulerMethod.deltaVelocity(body, force, elapsedTimeInSeconds);
			Vector2f deltaPosition = _eulerMethod.deltaPosition(body, force, elapsedTimeInSeconds);
			body.apply(deltaVelocity, deltaPosition);
		}
	}

}
