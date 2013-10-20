package ar.edu.itba.pedestriansim;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.parser.PedestrianFileParser;
import ar.edu.itba.pedestriansim.gui.Camera;
import ar.edu.itba.pedestriansim.gui.CameraHandler;
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
	private static final int MAX_DELTA = 15;
//	private static final float UPDATE_INTERVAL = 0.01f;
//	private long elapsedTimeMillis = 0;

	private PedestrianSim simulation;
	private Camera _camera;
	private PedestrianRenderer _pedestrianRenderer;

	public PedestrianApp(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		_camera = new Camera();
		_pedestrianRenderer = new PedestrianRenderer(_camera);
		simulation = new PedestrianSim();
		gc.getInput().addKeyListener(new CameraHandler(_camera));
		try {
			new PedestrianFileParser()
				.laod(simulation, new File("./src/main/resources/pedestrian.properties"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_pedestrianRenderer.render(gc, g, simulation.getScene().getPedestrians());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		delta = Math.min(delta, MAX_DELTA); // Cap max update interval!
//		elapsedTimeMillis += delta;
//		if ((UPDATE_INTERVAL_SECONDS * 1000) < elapsedTimeMillis) {
			simulation.update(gc, delta / 1000.0f);
//			elapsedTimeMillis = 0;
//		}
	}
}
