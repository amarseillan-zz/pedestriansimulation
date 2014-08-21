package ar.edu.itba.pedestriansim.back.entity;

import ar.edu.itba.pedestriansim.back.entity.force.PedestrianForce;
import ar.edu.itba.pedestriansim.back.entity.force.RepulsionForce;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;

public class PedestrianForces {

	/**
	 * Fuerza de repulsion entre dos pocisiones
	 */
	private RepulsionForce repulsionForceModel;

	/**
	 * Fuerza de repulsion entre una pared y una pocision
	 */
	private RepulsionForce wallRepulsionForceModel;

	/**
	 * Limite inferior para la aplicacion de la suma de los valores obtenidos
	 * con @repulsionForceModel
	 */
	private float externalForceThreshold;
	private float externalForceRadiusThreshold;

	/**
	 * Fuerza con la que un peaton se acerca a su future
	 */
	private PedestrianForce desireForce;

	/**
	 * Fuerza que el peaton aplica sobre su propio future para alcanzar su
	 * objetivo
	 */
	private PedestrianForce forceOnFuture;

	/**
	 * Fuerza de choque entre objetos
	 */
	private SpringForceModel collisitionModel;

	public void setRepulsionForceModel(RepulsionForce repulsionForceModel) {
		this.repulsionForceModel = repulsionForceModel;
	}

	public RepulsionForce getRepulsionForceModel() {
		return repulsionForceModel;
	}

	public void setDesireForce(PedestrianForce desireForce) {
		this.desireForce = desireForce;
	}

	public PedestrianForce getDesireForce() {
		return desireForce;
	}

	public void setCollisitionModel(SpringForceModel collisitionModel) {
		this.collisitionModel = collisitionModel;
	}

	public SpringForceModel getCollisitionModel() {
		return collisitionModel;
	}

	public void setExternalForceThreshold(float externalForceThreshold) {
		this.externalForceThreshold = externalForceThreshold;
	}

	public float getExternalForceThreshold() {
		return externalForceThreshold;
	}

	public void setExternalForceRadiusThreshold(float externalForceRadiusThreshold) {
		this.externalForceRadiusThreshold = externalForceRadiusThreshold;
	}

	public float getExternalForceRadiusThreshold() {
		return externalForceRadiusThreshold;
	}

	public void setForceOnFuture(PedestrianForce forceOnFuture) {
		this.forceOnFuture = forceOnFuture;
	}

	public PedestrianForce getForceOnFuture() {
		return forceOnFuture;
	}
	
	public void setWallRepulsionForceModel(RepulsionForce wallRepulsionForceModel) {
		this.wallRepulsionForceModel = wallRepulsionForceModel;
	}
	
	public RepulsionForce getWallRepulsionForceModel() {
		return wallRepulsionForceModel;
	}
}
