package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.RepulsionForceValues;

public interface RepulsionForce {

	Vector2f between(Vector2f from, Vector2f point, RepulsionForceValues values);

}
