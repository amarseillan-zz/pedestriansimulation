package ar.edu.itba.pedestriansim.back.desireforce;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.DrivingForce;
import ar.edu.itba.pedestriansim.back.Pedestrian;

public class ReactionDistanceDesireForce implements DesireForce {

	private final DrivingForce forceModel;
	
	public ReactionDistanceDesireForce() {
		forceModel = new DrivingForce();
	}
	
	@Override
	public Vector2f exertedBy(Pedestrian subject) {
		Vector2f target = subject.getFuture().getBody().getCenter();
		float distance = subject.getBody().getCenter().distance(target);
		float p = distance / subject.getReactionDistance();
		return forceModel.getForce(subject.getBody(), target, subject.getMaxVelocity()).scale(p);
	}

}
