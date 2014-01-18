package ar.edu.itba.pedestriansim.factory.component;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.component.PedestrianForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.desireforce.DesireForce;
import ar.edu.itba.pedestriansim.back.desireforce.ReactionDistanceDesireForce;
import ar.edu.itba.pedestriansim.physics.SpringForceModel;

public class PedestrianForceUpdaterFactory {
	
	private PedestrianAppConfig _config;
	
	public PedestrianForceUpdaterFactory(PedestrianAppConfig config) {
		_config = config;
	}
	
	private static enum DesireForceType {REACTION_DISTANCE};
	
	public Updateable produce(PedestrianArea pedestrianArea) {
		float springConstant = _config.get("springConstant", Float.class);
		return new PedestrianForceUpdaterComponent(pedestrianArea, buildDesireForce(), new SpringForceModel(springConstant));
	}
	
	private DesireForce buildDesireForce() {
		DesireForce desireForce;
		DesireForceType desireForceType = _config.getEnum(DesireForceType.class);
		switch (desireForceType) {
			case REACTION_DISTANCE:
				desireForce = new ReactionDistanceDesireForce();
				break;
			default:
				throw new IllegalStateException("Unknown type for DesireForceType: " + desireForceType);
		}
		return desireForce;
	}
}
