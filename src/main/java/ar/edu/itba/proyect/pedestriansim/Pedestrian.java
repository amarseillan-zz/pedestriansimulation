package ar.edu.itba.proyect.pedestriansim;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Pedestrian {

	private Shape shape;
	private Vector2f velocity;
	private float mass;
	private Force externalForce;

	public Pedestrian(Shape shape, float mass) {
		this.shape = shape;
		velocity = new Vector2f();
		this.mass = mass;
		externalForce = new Force();
	}

	public Shape getShape() {
		return shape;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void apply(Force force) {
		externalForce.add(force);
	}

	public Force getExternalForce() {
		return externalForce;
	}

	public float getMass() {
		return mass;
	}
	
	public void updatePosition(float elapsedTimeSeconds) {
		Force desireForce = new Force((velocity.length() < 10) ? new Vector2f(20f, 0) : new Vector2f());
		// A_(t) = Sum(F) / mass
		Vector2f acceleration = desireForce.add(externalForce).scale(1 / getMass()).getVector();
		// deltaV = A_(t) * dt
		Vector2f deltaVelocity = new Vector2f(acceleration).scale(elapsedTimeSeconds);
		// deltaX = V_(t) * dt + A_(t) * 0.5 * dt * dt
		Vector2f deltaPosition = new Vector2f()
			.add(new Vector2f(velocity).scale(elapsedTimeSeconds))
			.add(new Vector2f(acceleration).scale(0.5f * elapsedTimeSeconds * elapsedTimeSeconds));
		// Update values
		velocity.add(deltaVelocity);
		shape = shape.transform(Transform.createTranslateTransform(deltaPosition.x, deltaPosition.y));
	}
}































