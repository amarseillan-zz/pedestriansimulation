package ar.edu.itba.pedestriansim;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.gui.Camera;
import ar.edu.itba.pedestriansim.gui.CameraHandler;
import ar.edu.itba.pedestriansim.gui.PedestrianAreaRenderer;
import ar.edu.itba.pedestriansim.parser.AreaFileParser;

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

	private PedestrianSim simulation;
	private Camera _camera;
	private PedestrianAreaRenderer _renderer;

	public PedestrianApp(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		_camera = new Camera();
		_renderer = new PedestrianAreaRenderer(_camera);
		simulation = new PedestrianSim();
		gc.getInput().addKeyListener(new CameraHandler(_camera));
		File file = new File("./src/main/resources/room1.desc");
		new AreaFileParser().load(simulation, file);
//		try {
//			new PedestrianFileParser()
//				.laod(simulation, new File("./src/main/resources/pedestrian.properties"));
//		} catch (IOException e) {
//			throw new IllegalStateException(e);
//		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_renderer.render(gc, g, simulation.getScene());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		delta = Math.min(delta, MAX_DELTA); // Cap max update interval!
		simulation.update(gc, delta / 1000.0f);
	}
}
