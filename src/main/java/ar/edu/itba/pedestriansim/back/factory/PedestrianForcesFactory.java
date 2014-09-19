package ar.edu.itba.pedestriansim.back.factory;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.force.ExponentialRepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.force.PedestrianFutureAdjustmentForce;
import ar.edu.itba.pedestriansim.back.entity.force.ReactionDistanceDesireForce;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;

public class PedestrianForcesFactory {

	public PedestrianForces build(PedestrianAppConfig config) {
		PedestrianForces forces = new PedestrianForces();
		forces.setRepulsionForceModel(new ExponentialRepulsionForce(config.alpha(), config.beta()));
		forces.setWallRepulsionForceModel(new ExponentialRepulsionForce(config.wallAlpha(), config.wallBeta()));
		forces.setExternalForceRadiusThreshold(config.getExternalForceRadiusThreshold());
		forces.setExternalForceThreshold(config.getExternalForceThreshold());
		forces.setDesireForce(new ReactionDistanceDesireForce());
		forces.setForceOnFuture(new PedestrianFutureAdjustmentForce(150, 10, 50));
		forces.setCollisitionModel(new SpringForceModel());
		return forces;
	}

}
