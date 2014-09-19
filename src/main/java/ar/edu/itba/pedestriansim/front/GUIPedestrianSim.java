package ar.edu.itba.pedestriansim.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import ar.edu.itba.command.CommandParam;
import ar.edu.itba.command.CommandParser;
import ar.edu.itba.command.ParsedCommand;
import ar.edu.itba.pedestriansim.back.PedestrianSimApp;
import ar.edu.itba.pedestriansim.back.config.ApplicationConfigBuilder;
import ar.edu.itba.pedestriansim.back.config.CrossingConfig;
import ar.edu.itba.pedestriansim.back.config.PedestrianConfigurationFromFile;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSim;
import ar.edu.itba.pedestriansim.back.logic.PedestrianAreaStep;

import com.google.common.base.Predicate;
import com.google.common.io.Closer;

public class GUIPedestrianSim extends BasicGame {

	public static final Logger logger = Logger.getLogger(GUIPedestrianSim.class);
	public static final CommandParser parser;
	static {
		parser = new CommandParser()
			.param(new CommandParam("-config").message("Archivo (.properties) de donde se va a leer la configuracion de la aplicacion."))
		;
	}

	public static void main(String[] args) throws SlickException, IOException {
		ParsedCommand cmd = parser.parse(args);
		if (cmd.hasErrors()) {
			System.out.println(cmd.getErrorString());
			return;
		}
		ApplicationConfigBuilder configBuilder;
		if (cmd.hasParam("-config")) {
			Properties properties = new Properties();
			properties.load(new FileInputStream(cmd.param("-config")));
			configBuilder = new CrossingConfig(new PedestrianConfigurationFromFile(properties));
		} else {
			configBuilder = new CrossingConfig();
		}
		AppGameContainer appContainer = new AppGameContainer(new GUIPedestrianSim(configBuilder));
		appContainer.setUpdateOnlyWhenVisible(false);
		appContainer.setDisplayMode(1200, 700, false);
		appContainer.start();
	}

	private PedestrianAppConfig _config;
	private Camera _camera;
	private PedestrianSim _simulation;
	private PedestrianAreaRenderer _renderer;
	private boolean _simulationIsFinished = false;

	public GUIPedestrianSim(ApplicationConfigBuilder configBuilder) {
		super("Pedestrian simulation");
		_config = configBuilder.get();
		if (_config.isMakeNewRun()) {
			logger.info("Creating a new run");
			new PedestrianSimApp(_config).run();
		} else {
			logger.info("Assuming already defined run on given configuration");
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		final Closer closer = Closer.create();
		Scanner staticReader = closer.register(newScanner(_config.staticfile()));
		Scanner dynamicReader = closer.register(newScanner(_config.dynamicfile()));
		PedestrianAreaFileSerializer serializer = new PedestrianAreaFileSerializer();
		_simulation = new PedestrianSim(_config.pedestrianArea())
			.cutCondition(new Predicate<PedestrianArea>() {
				@Override
				public boolean apply(PedestrianArea input) {
					return _simulationIsFinished;
				}
			})
			.onStep(new UpdatePositionsFromFile(serializer.staticFileInfo(staticReader), serializer.steps(dynamicReader)))
			.onEnd(new PedestrianAreaStep() {
				@Override
				public void update(PedestrianArea input) {
					try {
						closer.close();
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			})
		;
		_camera = new Camera();
		_camera.setZoom(20f);
//		_camera.scrollX(1200);
		_renderer = new PedestrianAreaRenderer(_camera);
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(60);
		gc.getInput().addKeyListener(new KeyHandler(_camera, _renderer, gc));
	}

	private Scanner newScanner(File file) {
		try {
			return new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_renderer.render(gc, g, _simulation.pedestrianArea());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		_camera.update(gc);
		if (gc.getInput().isKeyDown(Input.KEY_X) || gc.getInput().isKeyDown(Input.KEY_ESCAPE) || _simulation.isFinished()) {
			System.out.println("Simulation finished... Exiting application!");
			gc.exit();
			return;
		}
		if (delta > 0) { // 0 = means paused!
			_simulation.step();
		}
		if (gc.getInput().isKeyDown(Input.KEY_R)) {
			gc.reinit();
		}
	}
}
