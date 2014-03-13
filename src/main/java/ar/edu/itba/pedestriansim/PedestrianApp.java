package ar.edu.itba.pedestriansim;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.mision.PedestrianTargetArea;
import ar.edu.itba.pedestriansim.factory.PedestrianFactory;
import ar.edu.itba.pedestriansim.gui.Camera;
import ar.edu.itba.pedestriansim.gui.KeyHandler;
import ar.edu.itba.pedestriansim.gui.PedestrianAreaRenderer;
import ar.edu.itba.pedestriansim.gui.PedestrianMouseController;

@Component
public class PedestrianApp extends BasicGame {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		context.refresh();
		try {
			AppGameContainer appContainer = new AppGameContainer(
					context.getBean(PedestrianApp.class));
			appContainer.setUpdateOnlyWhenVisible(false);
			appContainer.setDisplayMode(1200, 700, false);
			appContainer.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		context.close();
	}

	private static final float TIME_STEP = 1 / 100f;

	@Autowired
	private PedestrianAppConfig _config;

	private Camera _camera;
	private PedestrianSim _simulation;
	private PedestrianAreaRenderer _renderer;

	public PedestrianApp() {
		super("Pedestrian simulation");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		_camera = new Camera();
		_simulation = new PedestrianSim(_config, _camera);
		_renderer = new PedestrianAreaRenderer(_camera);
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(60);
		gc.getInput().addKeyListener(new KeyHandler(_camera, _renderer, gc));
		if ("true".equalsIgnoreCase(_config.get("mouseEnabled"))) {
			PedestrianMouseController mouseController = createMouseControlledPedestrian(gc);
			mouseController.setInput(gc.getInput()); // FIXME: no se porque Slick no esta llamando a este metodo
			gc.getInput().addMouseListener(mouseController);
		}
	}

	private PedestrianMouseController createMouseControlledPedestrian(
			GameContainer gc) {
		PedestrianFactory factory = new PedestrianFactory(_config);
		// FIXME: safely delete these lines of codes, used for debugging
		PedestrianMision mission = new PedestrianMision();
		mission.putFirst(new PedestrianTargetArea(new Circle(15, 15, 0.5f)));
		_simulation.getPedestrianArea().addPedestrian(
				factory.build(new Vector2f(10, 10), 1, mission));
		// ===================
		Pedestrian pedestrian = factory.build(new Vector2f(), 0,
				new PedestrianMision());
		_simulation.getPedestrianArea().addPedestrian(pedestrian);
		return new PedestrianMouseController(pedestrian, _camera);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_renderer.render(gc, g, _simulation.getPedestrianArea());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		_camera.update(gc);
		if (delta > 0) { // 0 = means paused!
			_simulation.update(TIME_STEP);
		}
		if (gc.getInput().isKeyDown(Input.KEY_R)) {
			gc.reinit();
		}
	}
}
