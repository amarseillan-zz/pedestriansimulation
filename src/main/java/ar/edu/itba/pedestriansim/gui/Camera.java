package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Camera {

	private static final float offset = 5;

	private Transform transform = new Transform();

	public void zoomOut() {
		transform.concatenate(Transform.createScaleTransform(0.9f, 0.9f));
	}

	public void zoomIn() {
		transform.concatenate(Transform.createScaleTransform(1.1f, 1.1f));
	}

	public void scrollLeft() {
		transform.concatenate(Transform.createTranslateTransform(offset, 0));
	}

	public void scrollRight() {
		transform.concatenate(Transform.createTranslateTransform(-offset, 0));
	}

	public void scrollUp() {
		transform.concatenate(Transform.createTranslateTransform(0, offset));
	}

	public void scrollDown() {
		transform.concatenate(Transform.createTranslateTransform(0, -offset));
	}

	public Shape transform(Shape shape) {
		return shape.transform(transform);
	}

	public Vector2f transform(Vector2f vector) {
		return transform.transform(vector);
	}
}
