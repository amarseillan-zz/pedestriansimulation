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
            app.setUpdateOnlyWhenVisible(false);
            app.setDisplayMode(1200, 700, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}

	private static final float TIME_STEP = 1 / 100f;

	private Camera _camera;
	private PedestrianAreaRenderer _renderer;
	private PedestrianSim _simulation;
	
	public PedestrianApp(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(60);
		_camera = new Camera();
		_simulation = new PedestrianSim();
		_renderer = new PedestrianAreaRenderer(_camera);
		gc.getInput().addKeyListener(new KeyHandler(gc, _camera, _renderer));
		new AreaFileParser().load(_simulation, new File("./src/main/resources/room1.desc"), _camera);
//		new PedestrianFileParser().load(_simulation, new File("./src/main/resources/pedestrian.properties"));
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_renderer.render(gc, g, _simulation.getScene());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		_camera.update(gc);
		_simulation.update(TIME_STEP);
	}
}
