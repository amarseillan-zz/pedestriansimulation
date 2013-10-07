package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.Scene;

public class PedestrianApp extends BasicGame {

	private final static int INTERVAL_UPDATE_MILLIS = 200;
	
	public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new PedestrianApp("Pedestrian simulation"));
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}

	private int timeFromLastUpdateMillis = 0; 
	private PedestrianSim simulation;
	private PedestrianRenderer pedestrianRenderer = new PedestrianRenderer();
	private ObstacleRenderer obstacleRenderer = new ObstacleRenderer();

	public PedestrianApp(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		simulation = new PedestrianSim();
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		Scene scene = simulation.getScene();
		for (Shape shape : scene.getObstacles()) {
			obstacleRenderer.render(g, shape);
		}
		for (Pedestrian pedestrian : scene.getPedestrians()) {
			pedestrianRenderer.render(g, pedestrian);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		timeFromLastUpdateMillis += delta;
		if (timeFromLastUpdateMillis >= INTERVAL_UPDATE_MILLIS) {
			float seconds = timeFromLastUpdateMillis / 1000f;
			simulation.update(gc, seconds);
			timeFromLastUpdateMillis = 0;
		}
	}
}
