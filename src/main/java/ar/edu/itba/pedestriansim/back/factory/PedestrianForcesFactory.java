package ar.edu.itba.pedestriansim.back.factory;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.force.ExponentialRepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.force.ReactionDistanceDesireForce;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;
import ar.edu.itba.pedestriansim.back.entity.force.SpringFutureAdjustementForce;

public class PedestrianForcesFactory {

	public PedestrianForces build(PedestrianAppConfig config) {
		PedestrianForces forces = new PedestrianForces();
		forces.setRepulsionForceModel(new ExponentialRepulsionForce(config.alpha(), config.beta()));
		forces.setWallRepulsionForceModel(new ExponentialRepulsionForce(config.wallAlpha(), config.wallBeta()));
		forces.setExternalForceRadiusThreshold(config.getExternalForceRadiusThreshold());
		forces.setExternalForceThreshold(config.getExternalForceThreshold());
		forces.setDesireForce(new ReactionDistanceDesireForce());
		forces.setForceOnFuture(new SpringFutureAdjustementForce(100, 10));
		forces.setCollisitionModel(new SpringForceModel());
		return forces;
	}

}
