package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;


public class PedestrianFuture {

	private RigidBody _body;

	public PedestrianFuture(Pedestrian owner) {
		_body = new RigidBody(1, new Vector2f(), 0.1f);
	}

	public RigidBody getBody() {
		return _body;
	}

}
