package ar.edu.itba.pedestriansim.front;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class ShapeRenderer {

	private Camera _camera;

	public ShapeRenderer(Camera camera) {
		_camera = camera;
	}

	public void draw(Graphics g, Shape shape, Color color) {
		g.setColor(color);
		draw(g, shape);
	}

	public void draw(Graphics g, Shape shape) {
		g.draw(_camera.transform(shape));
	}

	public void fill(Graphics g, Shape shape, Color color) {
		g.setColor(color);
		fill(g, shape);
	}

	public void fill(Graphics g, Shape shape) {
		g.fill(_camera.transform(shape));
	}

	public void drawString(Graphics g, String str, float x, float y) {
		Vector2f transform = _camera.transform(new Vector2f(x, y));
		g.drawString(str, transform.x, transform.y);
	}

	public Camera getCamera() {
		return _camera;
	}
}
