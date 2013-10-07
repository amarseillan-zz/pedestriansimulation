package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public class ObstacleRenderer {

	public void render(Graphics g, Shape obstacle) {
		g.setColor(Color.blue);
		g.fill(obstacle);
	}

}
