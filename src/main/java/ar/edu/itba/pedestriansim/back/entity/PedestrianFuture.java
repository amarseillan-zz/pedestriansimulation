package ar.edu.itba.pedestriansim.back.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;

public class PedestrianFuture {

	private RigidBody _body;

	public PedestrianFuture(float mass, Pedestrian owner) {
		_body = new RigidBody(mass, new Vector2f(), 0.1f);
	}

	public RigidBody getBody() {
		return _body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("body", getBody()).toString();
	}

}
