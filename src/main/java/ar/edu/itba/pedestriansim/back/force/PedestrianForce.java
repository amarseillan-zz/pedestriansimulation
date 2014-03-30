package ar.edu.itba.pedestriansim.back.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;

import com.google.common.base.Function;

public interface PedestrianForce extends Function<Pedestrian, Vector2f> {

}
