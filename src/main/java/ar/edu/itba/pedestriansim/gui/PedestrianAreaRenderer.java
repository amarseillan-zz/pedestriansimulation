package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.PedestrianArea;

public class PedestrianAreaRenderer extends ShapeRenderer {

	private PedestrianRenderer _pedestrianRenderer;
	
	public PedestrianAreaRenderer(Camera camera) {
		super(camera);
		_pedestrianRenderer = new PedestrianRenderer(camera);
	}

	public void render(GameContainer gc, Graphics g, PedestrianArea pedestrianArea) {
		_pedestrianRenderer.render(gc, g, pedestrianArea.getPedestrians());
		g.setColor(Color.white);
		for (Shape obstacle : pedestrianArea.getObstacles()) {
			fill(g, obstacle);
		}
	}
}
