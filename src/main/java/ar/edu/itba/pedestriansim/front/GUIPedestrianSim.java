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
import ar.edu.itba.pedestriansim.back.config.DefaultPedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.config.HallwayConfig;
import ar.edu.itba.pedestriansim.back.config.PedestrianConfigurationFromFile;
import ar.edu.itba.pedestriansim.back.config.SquareRoomConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSim;
import ar.edu.itba.pedestriansim.back.logic.PedestrianAreaStep;

import com.google.common.base.Predicate;
import com.google.common.io.Closer;

public class GUIPedestrianSim extends BasicGame {

	private static final int FPS = 80;

	public static final Logger logger = Logger.getLogger(GUIPedestrianSim.class);
	public static final CommandParser parser;
	static {
		parser = new CommandParser()
			.param(new CommandParam("-config").message("Archivo (.properties) de donde se va a leer la configuracion de la aplicacion."))
			.param(new CommandParam("-map").required().constrained("cross", "hallway", "room").message("Mapa en el cual correr la simulacion"))
			.param(new CommandParam("-static").message("Archivo con informacion estatica de los peatones"))
			.param(new CommandParam("-dynamic").message("Archivo con informacion dinamica de los peatones"))
			.param(new CommandParam("-door").message("Tamano de la puera (-map room unicamente!)"))
			.param(new CommandParam("-amount").message("Cantidad total de peatones(-map room unicamente!)"))
			.param(new CommandParam("-time").message("Tiempo de simulacion en segundos"))
			.param(new CommandParam("-outdir").message("Directorio donde guardar los archivos de salida"))
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
			configBuilder = new PedestrianConfigurationFromFile(properties);
		} else {
			String outdir = cmd.hasParam("-outdir") ? cmd.param("-outdir") : "";
			configBuilder = new DefaultPedestrianAppConfig(outdir);
		}
		String mapName = cmd.param("-map").toLowerCase();
		configBuilder = "cross".equals(mapName) ? new CrossingConfig(configBuilder) 
			: "hallway".equals(mapName) ? new HallwayConfig(configBuilder) 
			: new SquareRoomConfig(configBuilder);
		if (mapName.equals("room") && cmd.hasParam("-door")) {
			((SquareRoomConfig) configBuilder).setDoorWidth(Float.valueOf(cmd.param("-door")));
		}
		if (mapName.equals("room") && cmd.hasParam("-amount")) {
			((SquareRoomConfig) configBuilder).setDoorWidth(Integer.valueOf(cmd.param("-amount")));
		}
		PedestrianAppConfig config = configBuilder.get();
		if (cmd.hasParam("-static")) {
			config.setStaticfile(new File(cmd.param("-static")));
			config.makeNewRun(false);
		}
		if (cmd.hasParam("-dynamic")) {
			config.setDynamicfile(new File(cmd.param("-dynamic")));
			config.makeNewRun(false);
		}
		if (cmd.hasParam("-time")) {
			config.setSimulationTime(Float.valueOf(cmd.param("-time")));
		}
		AppGameContainer appContainer = new AppGameContainer(new GUIPedestrianSim(config));
		appContainer.setUpdateOnlyWhenVisible(false);
		appContainer.setDisplayMode(1200, 700, false);
		appContainer.start();
	}

	private GameContainer _gc;
	private PedestrianAppConfig _config;
	private Camera _camera;
	private PedestrianSim _simulation;
	private PedestrianAreaRenderer _renderer;
	private boolean _simulationIsFinished = false;
	private boolean _initialized;

	public GUIPedestrianSim(PedestrianAppConfig config) {
		super("Pedestrian simulation");
		_config = config;
		if (_config.isMakeNewRun()) {
			logger.info("Creating a new run");
			new PedestrianSimApp(_config).run();
		} else {
			logger.info("Assuming already defined run on given configuration");
		}
		_initialized = false;
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		_gc = gc;
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
		if (!_initialized) {
			_initialized = true;
			_camera = new Camera();
			_camera.setZoom(20f);
			_renderer = new PedestrianAreaRenderer(_camera);
			gc.setAlwaysRender(true);
			gc.setTargetFrameRate(FPS);
		}
		gc.getInput().addKeyListener(new KeyHandler(_camera));
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
		if (delta > 0) { // 0 = means paused!
			_simulation.step();
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		try {
			if (Input.KEY_R == key) {
				_gc.reinit();
			}
			if (Input.KEY_P == key) {
				_gc.setPaused(!_gc.isPaused());
			}
			if (Input.KEY_NUMPAD1 == key) {
				_gc.setTargetFrameRate((int) (_gc.getFPS() * 0.6f));
			}
			if (Input.KEY_NUMPAD2 == key) {
				_gc.setTargetFrameRate((int) (_gc.getFPS() * 1.2f));
			}
			if (Input.KEY_ESCAPE == key) {
				System.out.println("Simulation finished... Exiting application!");
				_gc.exit();
			}
			if (Input.KEY_C == key) {
				_renderer.toggleRenderDebugInfo();
			}
			if (Input.KEY_V == key) {
				_renderer.toggleRenderMoreDebugInfo();
			}
		} catch (SlickException e) {
			throw new RuntimeException(e);
		}
	}
}
