package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.Pedestrian;

public class PedestrianRenderer {

	public void render(Graphics g, Pedestrian pedestrian) {
		g.setColor(Color.green);
		Shape shape = pedestrian.getShape(); 
		g.fill(shape);
		g.drawString("v = " + pedestrian.getVelocity().length(), shape.getCenterX(), shape.getCenterY() - 20);
	}

}
