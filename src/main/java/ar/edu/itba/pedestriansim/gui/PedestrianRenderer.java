package ar.edu.itba.pedestriansim.gui;

import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;

public class PedestrianRenderer {

	private final Shape pedestrianShape = new Circle(0, 0, 10);
	
	public void render(GameContainer gc, Graphics g, Collection<Pedestrian> pedestrians) {
		for (Pedestrian pedestrian : pedestrians) {
			g.setColor(Color.green);
			Vector2f location = pedestrian.getLocation();
			pedestrianShape.setLocation(location);
			g.fill(pedestrianShape);
			String velocityString = String.format("v = %.3f [m/s]", pedestrian.getVelocity().length());
			g.drawString(velocityString, location.x, location.y - 20);
		}
	}

}
