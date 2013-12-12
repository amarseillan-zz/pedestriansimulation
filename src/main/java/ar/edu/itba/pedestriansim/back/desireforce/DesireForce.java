package ar.edu.itba.pedestriansim.back.desireforce;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;

public interface DesireForce {

	Vector2f exertedBy(Pedestrian subject);

}
