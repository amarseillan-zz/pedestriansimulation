package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Vector2f;

public interface RepulsionForce {

	Vector2f apply(Vector2f from, Vector2f other);

}
