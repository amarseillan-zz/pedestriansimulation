package ar.edu.itba.pedestriansim.gui;

import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;

public class PedestrianRenderer extends ShapeRenderer {

	private final Line externalForce = new Line(0, 0);
	private final Line pedestrianPath = new Line(0, 0);
	private final Shape pedestrianShape = new Circle(0, 0, 10);
	
	public PedestrianRenderer(Camera camera) {
		super(camera);
	}

	public void render(GameContainer gc, Graphics g, Collection<Pedestrian> pedestrians) {
		for (Pedestrian pedestrian : pedestrians) {
			drawPath(g, pedestrian);
			drawShape(g, pedestrian);
			drawStats(g, pedestrian);
		}
	}

	private void drawPath(Graphics g, Pedestrian pedestrian) {
		Vector2f location = pedestrian.getBody().getLocation();
		pedestrianPath.set(location, pedestrian.getTarget());
		draw(g, pedestrianPath, Color.white);
		externalForce.set(location, location.copy().add(pedestrian.getAppliedForce()));
		draw(g, externalForce, Color.orange);
	}
	
	private void drawShape(Graphics g, Pedestrian pedestrian) {
		Vector2f location = pedestrian.getBody().getLocation();
		pedestrianShape.setCenterX(location.x);
		pedestrianShape.setCenterY(location.y);
		fill(g, pedestrianShape, Color.green);
	}
	
	private void drawStats(Graphics g, Pedestrian pedestrian) {
		g.setColor(Color.cyan);
		Vector2f location = pedestrian.getBody().getLocation();
		float distance = location.distance(pedestrian.getTarget());
		String positionString = String.format("x = (%.2f, %.2f) | D = %.3f [m]", location.x, location.y, distance);
		drawString(g, positionString, location.x, location.y - 30);
		String velocityString = String.format("v = %.2f [m/s] | ETA(aprox) = %.3f", pedestrian.getBody().getVelocity().length(), pedestrian.getETA());
		drawString(g, velocityString, location.x, location.y - 15);
	}
	
}
