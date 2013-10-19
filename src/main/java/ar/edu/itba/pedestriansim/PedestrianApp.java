package ar.edu.itba.pedestriansim;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.parser.PedestrianFileParser;
import ar.edu.itba.pedestriansim.gui.ObstacleRenderer;
import ar.edu.itba.pedestriansim.gui.PedestrianRenderer;

public class PedestrianApp extends BasicGame {

	public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new PedestrianApp("Pedestrian simulation"));
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}

//	private static final float UPDATE_INTERVAL = 0.01f;
//	private long elapsedTimeMillis = 0;

	private PedestrianSim simulation;
	private PedestrianRenderer pedestrianRenderer = new PedestrianRenderer();
	private ObstacleRenderer obstacleRenderer = new ObstacleRenderer();

	public PedestrianApp(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		simulation = new PedestrianSim();
		try {
			new PedestrianFileParser()
				.laod(simulation, new File("./src/main/resources/pedestrian.properties"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		PedestrianArea scene = simulation.getScene();
		for (Shape shape : scene.getObstacles()) {
			obstacleRenderer.render(g, shape);
		}
		pedestrianRenderer.render(gc, g, scene.getPedestrians());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
//		elapsedTimeMillis += delta;
//		if ((UPDATE_INTERVAL_SECONDS * 1000) < elapsedTimeMillis) {
			simulation.update(gc, delta / 1000.0f);
//			elapsedTimeMillis = 0;
//		}
	}
}
