package ar.edu.itba.proyect.pedestriansim;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public class ShapeRenderer {

	private Color color = Color.red;

	public void setColor(Color color) {
		this.color = color;
	}

	public void render(Graphics g, Shape shape) {
		g.setColor(color);
		g.fill(shape);
	}

}
