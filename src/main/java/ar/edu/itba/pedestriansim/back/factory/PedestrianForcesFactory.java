package ar.edu.itba.pedestriansim.back.factory;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.force.ExponentialRepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.force.PedestrianFutureAdjustmentForce;
import ar.edu.itba.pedestriansim.back.entity.force.ReactionDistanceDesireForce;
import ar.edu.itba.pedestriansim.back.entity.force.ReactionDistanceDesireForce.FunctionType;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;

public class PedestrianForcesFactory {

	public PedestrianForces build(PedestrianAppConfig config) {
		PedestrianForces forces = new PedestrianForces();
		forces.setRepulsionForceModel(new ExponentialRepulsionForce());
		forces.setWallRepulsionForceModel(new ExponentialRepulsionForce());
		forces.setExternalForceRadiusThreshold(config.getExternalForceRadiusThreshold());
		forces.setExternalForceThreshold(config.getExternalForceThreshold());
		forces.setDesireForce(new ReactionDistanceDesireForce(FunctionType.LINEAR));
		forces.setForceOnFuture(new PedestrianFutureAdjustmentForce(150, 10, 100));
		forces.setCollisitionModel(new SpringForceModel());
		return forces;
	}

}
