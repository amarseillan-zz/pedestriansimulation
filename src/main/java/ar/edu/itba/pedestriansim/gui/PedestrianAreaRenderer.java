package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianSource;

public class PedestrianAreaRenderer extends ShapeRenderer {

	private boolean renderStats;
	private PedestrianRenderer _pedestrianRenderer;
	private final Circle _sourceShape = new Circle(0, 0, 5);

	public PedestrianAreaRenderer(Camera camera) {
		super(camera);
		_pedestrianRenderer = new PedestrianRenderer(camera, this);
	}

	public void render(GameContainer gc, Graphics g, PedestrianArea pedestrianArea) {
		g.setColor(Color.pink);
		for (PedestrianSource source : pedestrianArea.getSources()) {
			_sourceShape.setRadius(source.getRadius());
			_sourceShape.setCenterX(source.getLocation().x);
			_sourceShape.setCenterY(source.getLocation().y);
			fill(g, _sourceShape);
		}
		_pedestrianRenderer.render(gc, g, pedestrianArea.getPedestrians());
		g.setColor(Color.white);
		for (Shape obstacle : pedestrianArea.getObstacles()) {
			draw(g, obstacle);
		}
	}
	
	public void toggleRenderStats() {
		renderStats = !renderStats;
	}
	
	public boolean isRenderStats() {
		return renderStats;
	}
}
