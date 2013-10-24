package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Camera {

	private Transform transform = new Transform();
	private float width, height;

	public void update(GameContainer gc) {
		width = gc.getWidth();
		height = gc.getHeight();
	}
	
	public void zoomOut() {
		setZoom(0.95f);
	}

	public void zoomIn() {
		setZoom(1.05f);
	}
	
	public void setZoom(float zoom) {
		transform.concatenate(Transform.createScaleTransform(zoom, zoom));
	}
	
	public void scrollLeft() {
		scrollX(getHorizontalScroll());
	}

	public void scrollRight() {
		scrollX(-getHorizontalScroll());
	}

	public void scrollX(float amount) {
		transform.concatenate(Transform.createTranslateTransform(amount, 0));
	}
	
	public void scrollUp() {
		scrollY(getVerticalScroll());
	}

	public void scrollDown() {
		scrollY(-getVerticalScroll());
	}

	public void scrollY(float amount) {
		transform.concatenate(Transform.createTranslateTransform(0, amount));
	}

	private float getVerticalScroll() {
		return height * 0.005f;
	}
	
	private float getHorizontalScroll() {
		return width * 0.005f;
	}

	public Shape transform(Shape shape) {
		return shape.transform(transform);
	}

	public Vector2f transform(Vector2f vector) {
		return transform.transform(vector);
	}
}
