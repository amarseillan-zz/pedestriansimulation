package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Pedestrian {

	private static final float MASS = 50;

	private Shape shape;
	private float mass;
	private Carrot carrot;
	private Vector2f velocity;

	public Pedestrian(float x, float y) {
		this.shape = new Circle(x, y, 10);
		this.mass = MASS;
		carrot = new Carrot(new Vector2f(1, 0));
		velocity = new Vector2f();
	}

	public Shape getShape() {
		return shape;
	}

	public Vector2f getVelocity() {
		return velocity.copy();
	}

	public void addVelocity(Vector2f velocity) {
		this.velocity.add(velocity);
	}

	public Vector2f getDesireVelocity() {
		return new Vector2f(20, 0);
	}

	public float getMass() {
		return mass;
	}

	public Carrot getCarrot() {
		return carrot;
	}

	public void apply(Transform transform) {
		shape = shape.transform(transform);
		carrot.apply(transform);
	}

}
