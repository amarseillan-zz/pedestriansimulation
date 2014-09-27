package ar.edu.itba.pedestriansim.front;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;

public class PedestrianAreaRenderer extends ShapeRenderer {

	private boolean renderDebugInfo;
	private boolean renderMoreDebugInfo;
	private PedestrianRenderer _pedestrianRenderer;
	private final Circle _sourceShape = new Circle(0, 0, 5);

	public PedestrianAreaRenderer(Camera camera) {
		super(camera);
		_pedestrianRenderer = new PedestrianRenderer(camera, this);
	}

	public void render(GameContainer gc, Graphics g, PedestrianArea pedestrianArea) {
		g.setColor(Color.pink);
		for (PedestrianSource source : pedestrianArea.sources()) {
			_sourceShape.setRadius(source.radius());
			_sourceShape.setCenterX(source.center().x);
			_sourceShape.setCenterY(source.center().y);
			fill(g, _sourceShape);
		}
		_pedestrianRenderer.render(gc, g, pedestrianArea.pedestrians());
		g.setColor(Color.white);
		for (Shape obstacle : pedestrianArea.obstacles()) {
			draw(g, obstacle);
		}
	}

	public void toggleRenderDebugInfo() {
		renderDebugInfo = !renderDebugInfo;
	}

	public void toggleRenderMoreDebugInfo() {
		renderMoreDebugInfo = !renderMoreDebugInfo;
	}

	public boolean isRenderDebugInfo() {
		return renderDebugInfo;
	}

	public boolean isRenderMoreDebugInfo() {
		return renderMoreDebugInfo;
	}
}
