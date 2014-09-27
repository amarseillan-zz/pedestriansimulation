package ar.edu.itba.pedestriansim.front;

import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;

public class PedestrianRenderer extends ShapeRenderer {

	private final Color[] TEAM_COLORS = { Color.green, Color.red, Color.blue, Color.white };

	private PedestrianAreaRenderer _pedestrianArearenderer;
	private final Line future = new Line(0, 0);
	private final Line pedestrianPath = new Line(0, 0);
	private final Circle pedestrianShape = new Circle(0, 0, 1);

	public PedestrianRenderer(Camera camera, PedestrianAreaRenderer pedestrianArearenderer) {
		super(camera);
		_pedestrianArearenderer = pedestrianArearenderer;
	}

	public void render(GameContainer gc, Graphics g, Collection<Pedestrian> pedestrians) {
		for (Pedestrian pedestrian : pedestrians) {
			if (_pedestrianArearenderer.isRenderDebugInfo()) {
				drawPath(g, pedestrian);
				drawShape(g, pedestrian.getFuture().getBody(), 3);
				Vector2f center = pedestrian.getBody().getCenter();
				if (_pedestrianArearenderer.isRenderMoreDebugInfo()) {
					float alpha = pedestrian.pedestrianRepulsionForceValues().alpha();
					float beta = pedestrian.pedestrianRepulsionForceValues().beta();
					String str = String.format("{%.0f,%.3f}", alpha, beta);
					drawString(g, str, center.x, center.y);
				}
			}
			drawShape(g, pedestrian.getBody(), pedestrian.getTeam());
		}
		g.setColor(Color.cyan);
		g.drawString("Pedestrians: " + pedestrians.size(), 0, gc.getHeight() - 20);
	}

	private void drawPath(Graphics g, Pedestrian pedestrian) {
		if (pedestrian.getTargetSelection().getTarget() != null) {
			Vector2f location = pedestrian.getBody().getCenter();
			pedestrianPath.set(location, pedestrian.getTargetSelection().getTarget().getClosesPoint(location));
			draw(g, pedestrianPath, Color.white);
			future.set(location, pedestrian.getFuture().getBody().getCenter());
			draw(g, future, Color.red);

		}
	}

	private void drawShape(Graphics g, RigidBody pedestrian, int color) {
		Vector2f location = pedestrian.getCenter();
		pedestrianShape.setRadius(pedestrian.getRadius());
		pedestrianShape.setCenterX(location.x);
		pedestrianShape.setCenterY(location.y);
		fill(g, pedestrianShape, TEAM_COLORS[color]);
		draw(g, pedestrianShape, Color.white);
	}

}
