package ar.edu.itba.pedestriansim.front;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.edu.itba.pedestriansim.back.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianSimApp;
import ar.edu.itba.pedestriansim.back.component.Component;
import ar.edu.itba.pedestriansim.back.component.UpdatePedestrialPositionFromFileComponent;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSim;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;
import ar.edu.itba.pedestriansim.back.factory.SimulationComponentsFactory;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

@org.springframework.stereotype.Component
public class GUIPedestrianSim extends BasicGame {

	public static void main(String[] args) {
		if (args.length < 2) {
			args = new String[2];
			args[0] = "static.txt";
			args[1] = "dynamic.txt";
		}
		PedestrianSimApp.main(new String[] {args[0], args[1]});	// Run backend first!
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		context.refresh();
		try {
			GUIPedestrianSim sim = context.getBean(GUIPedestrianSim.class);
			sim._config.setStaticfile(args[0]);
			sim._config.setDynamicfile(args[1]);
			AppGameContainer appContainer = new AppGameContainer(sim);
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
	private boolean _simulationIsFinished;

	public GUIPedestrianSim() {
		super("Pedestrian simulation");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		_simulationIsFinished = false;
		_camera = new Camera();
		_simulation = new PedestrianSim(_config, new GUISimulationComponentsFactoryImpl(), new Predicate<PedestrianArea>() {
			@Override
			public boolean apply(PedestrianArea input) {
				return _simulationIsFinished;
			}
		});
		disableSources();
		setupView();
		_renderer = new PedestrianAreaRenderer(_camera);
		gc.setAlwaysRender(true);
		gc.setTargetFrameRate(60);
		gc.getInput().addKeyListener(new KeyHandler(_camera, _renderer, gc));
	}
	
	private void disableSources() {
		for (PedestrianSource source : _simulation.getPedestrianArea().getSources()) {
			source.disable();
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		_renderer.render(gc, g, _simulation.getPedestrianArea());
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		_camera.update(gc);
		if (_simulation.isFinished()) {
			System.out.println("Simulation finished... Exiting application!");
			gc.exit();
			return;
		}
		if (delta > 0) { // 0 = means paused!
			_simulation.update(TIME_STEP);
		}
		if (gc.getInput().isKeyDown(Input.KEY_R)) {
			gc.reinit();
		}
		if (gc.getInput().isKeyDown(Input.KEY_X)) {
			_simulation.end();
			gc.exit();
		}
	}
	

	private void setupView() {
		Optional<Integer> zoom = _config.getOptional("zoom", Integer.class);
		if (zoom.isPresent()) {
			_camera.setZoom(zoom.get());
		}
		Optional<String[]> location = _config.getLocation();
		if (location.isPresent()) {
			_camera.scrollX(Float.parseFloat(location.get()[0]));
			_camera.scrollY(Float.parseFloat(location.get()[1]));
		}
	}

	private class GUISimulationComponentsFactoryImpl implements SimulationComponentsFactory {

		@Override
		public List<Component> produce(PedestrianAppConfig config, PedestrianArea pedestrianArea) {
			List<Component> components = Lists.newLinkedList();
			try {
				PedestrianAreaFileSerializer serializer = new PedestrianAreaFileSerializer(pedestrianArea, new File(config.get("log.directory")), config.getStaticfile(), config.getDynamicfile());
				components.add(new UpdatePedestrialPositionFromFileComponent(pedestrianArea, serializer.staticFileInfo(), composeSteps(serializer.steps())));
			} catch (FileNotFoundException e) {
				throw new IllegalStateException(e);
			}
			return components;
		}
		
		private Supplier<DymaimcFileStep> composeSteps(final Supplier<DymaimcFileStep> steps) {
			return new Supplier<DymaimcFileStep>() {
				@Override
				public DymaimcFileStep get() {
					DymaimcFileStep step = steps.get();
					_simulationIsFinished = (step == null);
					return step;
				}
			};
		}

	}
}
