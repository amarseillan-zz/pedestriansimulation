package ar.edu.itba.pedestriansim.back.force;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.physics.DrivingForce;

public class ReactionDistanceDesireForce implements PedestrianForce {

	private final DrivingForce forceModel;
	
	public ReactionDistanceDesireForce() {
		forceModel = new DrivingForce();
	}
	
	@Override
	public Vector2f apply(Pedestrian subject) {
		Vector2f target = subject.getFuture().getBody().getCenter();
		float distance = subject.getBody().getCenter().distance(target);
		float p = distance / subject.getReactionDistance();
		return forceModel.getForce(subject.getBody(), target, subject.getMaxVelocity()).scale(p);
	}

}
