package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianSource;
import ar.edu.itba.pedestriansim.back.spatial.Grid;

public class PedestrianAreaRenderer extends ShapeRenderer {

	private boolean renderDebugInfo;
	private PedestrianRenderer _pedestrianRenderer;
	private final Circle _sourceShape = new Circle(0, 0, 5);

	public PedestrianAreaRenderer(Camera camera) {
		super(camera);
		_pedestrianRenderer = new PedestrianRenderer(camera, this);
	}

	@SuppressWarnings("rawtypes") 
	public void render(GameContainer gc, Graphics g, PedestrianArea pedestrianArea) {
		g.setColor(Color.pink);
		if (renderDebugInfo) {
			for (Grid[] grids : pedestrianArea.getMap().getGrid()) {
				for (Grid grid : grids) {
					Rectangle area = grid.getArea(); 
					draw(g, area, Color.green);
					drawString(g, grid.size() + "", area.getX(), area.getY());
				}
			}
		}
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
	
	public void toggleRenderDebugInfo() {
		renderDebugInfo = !renderDebugInfo;
	}
	
	public boolean isRenderDebugInfo() {
		return renderDebugInfo;
	}
}
