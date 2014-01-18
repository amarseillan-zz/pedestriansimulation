package ar.edu.itba.pedestriansim.factory.component;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Pedestrians;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.component.FutureForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.replusionforce.RepulsionForce;
import ar.edu.itba.pedestriansim.back.replusionforce.RepulsionForceModel1;

import com.google.common.base.Function;

public class FutureForceUpdaterComponentFactory {

	private static enum RepulsionForceType {MODEL_1};
	private static enum PedestrianInteractionType {BODY_LOCATION, FUTURE_LOCATION};
	
	private PedestrianAppConfig _config;
	
	public FutureForceUpdaterComponentFactory(PedestrianAppConfig config) {
		_config = config;
	}

	public Updateable produce(PedestrianArea pedestrianArea) {
		float externalForceThreshold  = _config.get("externalForceThreshold", Float.class);
		return new FutureForceUpdaterComponent(pedestrianArea, 
			buildInteractionLocation(), externalForceThreshold, buildRepulsionForce());
	}
	
	private Function<Pedestrian, Vector2f> buildInteractionLocation() {
		Function<Pedestrian, Vector2f> interactionLocation;
		PedestrianInteractionType interactionType = _config.getEnum(PedestrianInteractionType.class);
		switch (interactionType) {
			case BODY_LOCATION:
				interactionLocation = Pedestrians.getLocation();
				break;
			case FUTURE_LOCATION:
				interactionLocation = Pedestrians.getFutureLocation();
				break;
			default:
				throw new IllegalStateException("Unknown type for PedestrianInteractionType: " + interactionType);
		}
		return interactionLocation;
	}

	private RepulsionForce buildRepulsionForce() {
		RepulsionForce repulsionForce;
		RepulsionForceType repulsionForceType = _config.getEnum(RepulsionForceType.class);
		switch (repulsionForceType) {
			case MODEL_1:
				repulsionForce = new RepulsionForceModel1(
					_config.getEnumParam(repulsionForceType, "alpha", Float.class),
					_config.getEnumParam(repulsionForceType, "beta", Float.class));
				break;
			default:
				throw new IllegalStateException("Unknown type for RepulsionForceModel: " + repulsionForceType);
		}
		return repulsionForce;
	}
	
	
}
