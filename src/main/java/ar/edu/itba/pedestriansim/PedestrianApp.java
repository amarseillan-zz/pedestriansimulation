package ar.edu.itba.pedestriansim;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.gui.Camera;
import ar.edu.itba.pedestriansim.gui.KeyHandler;
import ar.edu.itba.pedestriansim.gui.PedestrianAreaRenderer;

@Component
public class PedestrianApp extends BasicGame {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		context.refresh();
		try {
			PedestrianApp app = context.getBean(PedestrianApp.class);
			AppGameContainer appContainer = new AppGameContainer(app);
			appContainer.setUpdateOnlyWhenVisible(false);
			appContainer.setDisplayMode(1200, 700, false);
			appContainer.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		context.close();
	}

	private static final float TIME_STEP = 1 / 100f;

	private Camera _camera;
	private PedestrianAreaRenderer _renderer;

	private PedestrianSim _simulation;
	
	@Autowired
	private PedestrianAppConfig _config;

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
		gc.getInput().addKeyListener(new KeyHandler(_camera, _renderer));
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
		} else if (gc.getInput().isKeyDown(Input.KEY_P)) {
			gc.setPaused(!gc.isPaused());
		}
	}
}
