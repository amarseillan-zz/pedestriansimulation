package ar.edu.itba.pedestriansim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
			AppGameContainer app = new AppGameContainer(new PedestrianApp("./src/main/resources/room1/setup.properties"));
			app.setUpdateOnlyWhenVisible(false);
			app.setDisplayMode(1200, 700, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private static final float TIME_STEP = 1 / 100f;

	private File _configFile;
	private Camera _camera;
	private PedestrianAreaRenderer _renderer;
	private PedestrianSim _simulation;

	public PedestrianApp(String configurationFilePath) {
		super("Pedestrian simulation");
		_configFile = new File(configurationFilePath);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		try {
			gc.setAlwaysRender(true);
			gc.setTargetFrameRate(60);
			_camera = new Camera();
			_renderer = new PedestrianAreaRenderer(_camera);
			gc.getInput().addKeyListener(new KeyHandler(gc, _camera, _renderer));
			Properties configuration = new Properties();
			configuration.load(new FileInputStream(_configFile));
			_simulation = new AreaFileParser().load(_configFile.getParentFile().getAbsolutePath(), _camera, configuration);
		} catch (IOException e) {
			throw new IllegalStateException("Error aprsing configuration file!", e);
		}
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
