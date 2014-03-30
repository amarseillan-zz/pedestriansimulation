package ar.edu.itba.pedestriansim.back.entity;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.force.PedestrianForce;
import ar.edu.itba.pedestriansim.back.physics.SpringForceModel;
import ar.edu.itba.pedestriansim.back.replusionforce.RepulsionForce;

import com.google.common.base.Function;

public class PedestrianForces {

	/**
	 * Valor que se va a utilizar para calcular la interaccion con un pedestrian
	 * future
	 */
	private Function<Pedestrian, Vector2f> interactionLocation;

	/**
	 * Fuerza de repulsion entre dos pocisiones
	 */
	private RepulsionForce repulsionForceModel;

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

	public void setInteractionLocation(Function<Pedestrian, Vector2f> interactionLocation) {
		this.interactionLocation = interactionLocation;
	}

	public Function<Pedestrian, Vector2f> getInteractionLocation() {
		return interactionLocation;
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
}
