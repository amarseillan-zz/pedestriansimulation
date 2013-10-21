package ar.edu.itba.pedestriansim;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.gui.Camera;
import ar.edu.itba.pedestriansim.gui.KeyHandler;
import ar.edu.itba.pedestriansim.gui.PedestrianAreaRenderer;
import ar.edu.itba.pedestriansim.parser.AreaFileParser;

public class PedestrianApp extends BasicGame {

	public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new PedestrianApp("Pedestrian simulation"));
            app.setDisplayMode(1200, 700, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}

	private static final int MAX_DELTA = 15;

	private Camera _camera;
	private PedestrianAreaRenderer _renderer;
	private PedestrianSim _simulation;
	private KeyHandler _cameraHandler;
	
	public PedestrianApp(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);
		_camera = new Camera();
		_simulation = new PedestrianSim();
		_renderer = new PedestrianAreaRenderer(_camera);
		_cameraHandler = new KeyHandler(_camera, _renderer);
		_cameraHandler.initialize(gc);
		gc.getInput().addKeyListener(_cameraHandler);
		new AreaFileParser().load(_simulation, new File("./src/main/resources/room1.desc"));
//		new PedestrianFileParser().load(_simulation, new File("./src/main/resources/pedestrian.properties"));
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_renderer.render(gc, g, _simulation.getScene());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		delta = Math.min(delta, MAX_DELTA); // Cap max update interval!
		float elapsedTimeInSeconds = delta / 1000.0f;
		_camera.update(gc);
		_simulation.update(elapsedTimeInSeconds);
	}
}
