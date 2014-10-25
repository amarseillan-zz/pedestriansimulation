package ar.edu.itba.pedestriansim.back.config;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianTargetArea;

public class CrossingConfig implements ApplicationConfigBuilder {

	private ApplicationConfigBuilder _defaultBuilder;
	
	public CrossingConfig() {
		this(new DefaultPedestrianAppConfig());
	}

	public CrossingConfig(ApplicationConfigBuilder defaultBuilder) {
		_defaultBuilder = defaultBuilder;
	}

	@Override
	public PedestrianAppConfig get() {
		PedestrianAppConfig config = _defaultBuilder.get();
		addWalls(config.pedestrianArea());
		addSource1(config.pedestrianArea());
		addSource2(config.pedestrianArea());
		return config;
	}

	private final int _W = 5;
	private final int _L = 10;

	private void addSource1(PedestrianArea area) {
		// Arriba
		PedestrianMision mission = new PedestrianMision();
		area.addSource(
			new PedestrianSource(new Vector2f(_L + _W / 2, -1f), 0.5f, mission, 1)
				.setProduceDelayGenerator(new UniformRandomGenerator(1, 2f))
				.setPedestrianAmountGenerator(new UniformRandomGenerator(1, 1))
		);
		mission.putFirst(new PedestrianTargetArea(new Line(_L, 2 * _L + _W,  _L + _W, 2 * _L + _W)));
	}

	private void addSource2(PedestrianArea area) {
		// Derecha
		PedestrianMision mission = new PedestrianMision();
		area.addSource(
			new PedestrianSource(new Vector2f(2 * _L + _W, _L + _W / 2), 0.5f, mission, 2)
				.setProduceDelayGenerator(new UniformRandomGenerator(1, 2f))
				.setPedestrianAmountGenerator(new UniformRandomGenerator(1, 1f))
		);
		mission.putFirst(new PedestrianTargetArea(new Line(0, _L, 0, _L + _W)));
	}
	
	private void addWalls(PedestrianArea area) {
		// Arriba izquierda
		area.addObstacle(new Line(_L, 0, _L, _L));
		area.addObstacle(new Line(0, _L, _L, _L));
		// Arriba derecha
		area.addObstacle(new Line(_L + _W, 0, _L + _W, _L));
		area.addObstacle(new Line(_L + _W, _L, _L * 2 + _W, _L));
		// Abajo izquierda
		area.addObstacle(new Line(0, _L + _W, _L, _L + _W));
		area.addObstacle(new Line(_L, _L + _W, _L, _L * 2 + _W));
		// Abajo derecha
		area.addObstacle(new Line(_L + _W, _L + _W, _L * 2 + _W, _L + _W));
		area.addObstacle(new Line(_L + _W, _L + _W, _L + _W, _L * 2+ _W));
	}
}
