package ar.edu.itba.pedestriansim.back.entity;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.physics.RigidBody;

public class PedestrianFuture {

	private RigidBody _body;

	public PedestrianFuture(float mass, Pedestrian owner) {
		_body = new RigidBody(mass, new Vector2f(), 0.1f);
	}

	public RigidBody getBody() {
		return _body;
	}

}
