package ar.edu.itba.pedestriansim.front;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.front.contrib.slick.BetterTransform;

public class Camera {

	private BetterTransform transform = new BetterTransform();
	private float width, height;
	private static final float SMOOTHER = 20f;
	private static final float SCROLL = 0.63f;

	public void update(GameContainer gc) {
		width = gc.getWidth();
		height = gc.getHeight();
	}

	public void zoomOut() {
		setZoom(0.99f);
		this.scrollX(SCROLL);
		this.scrollY(SCROLL);
	}

	public void zoomIn() {
		setZoom(1.01f);
		this.scrollX(-SCROLL);
		this.scrollY(-SCROLL);
	}

	public void setZoom(float zoom) {
		transform.scale(zoom);
	}

	public void scrollLeft() {
		scrollX(getHorizontalScroll() / SMOOTHER);
	}

	public void scrollRight() {
		scrollX(-getHorizontalScroll() / SMOOTHER);
	}

	public void scrollX(float amount) {
		transform.translate(amount, 0);
	}

	public void scrollUp() {
		scrollY(getVerticalScroll() / SMOOTHER);
	}

	public void scrollDown() {
		scrollY(-getVerticalScroll() / SMOOTHER);
	}

	public void scrollY(float amount) {
		transform.translate(0, amount);
	}

	private float getVerticalScroll() {
		return height * 0.005f;
	}

	private float getHorizontalScroll() {
		return width * 0.005f;
	}

	public Shape transform(Shape shape) {
		return transform.transform(shape);
	}

	public Vector2f inverseTransform(Vector2f pt) {
		return transform.inverse().transform(pt);
	}

	public Vector2f transform(Vector2f vector) {
		return transform.transform(vector);
	}
}
