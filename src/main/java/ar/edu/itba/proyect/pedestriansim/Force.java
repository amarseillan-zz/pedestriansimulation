package ar.edu.itba.proyect.pedestriansim;

import org.newdawn.slick.geom.Vector2f;

public class Force {

	private Vector2f vector;

	public Force() {
		this(new Vector2f());
	}

	public Force(Vector2f vector) {
		this.vector = vector;
	}

	public Vector2f getVector() {
		return vector;
	}

	public Force add(Force other) {
		vector = vector.add(other.getVector());
		return this;
	}

	public Force substract(Force other) {
		vector = vector.sub(other.getVector());
		return this;
	}

	public Force scale(float factor) {
		vector = vector.scale(factor);
		return this;
	}

	public float mod() {
		return vector.length();
	}

	public boolean isNull() {
		return Math.abs(mod()) <= Float.MIN_VALUE;
	}
	
	@Override
	public String toString() {
		return getVector().toString();
	}
}
