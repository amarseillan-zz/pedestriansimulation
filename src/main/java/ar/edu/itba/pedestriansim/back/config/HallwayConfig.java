package ar.edu.itba.pedestriansim.back.config;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.GaussianRandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianTargetArea;

public class HallwayConfig implements ApplicationConfigBuilder {

	@Override
	public PedestrianAppConfig get() {
		PedestrianAppConfig config = new DefaultPedestrianAppConfig().get();
		addWalls(config.pedestrianArea());
		addSource1(config.pedestrianArea());
		addSource2(config.pedestrianArea());
		return config;
	}

	private void addSource1(PedestrianArea area) {
		// derecha
		PedestrianMision mission = new PedestrianMision();
		area.addSource(
			new PedestrianSource(new Vector2f(27, 17), 1f, mission, 1)
				.setProduceDelayGenerator(new UniformRandomGenerator(1, 2f))
				.setPedestrianAmountGenerator(new UniformRandomGenerator(1, 1))
		);
		mission.putFirst(new PedestrianTargetArea(new Line(10, 15, 10, 19)));
	}

	private void addSource2(PedestrianArea area) {
		// izquierda
		PedestrianMision mission = new PedestrianMision();
		area.addSource(
			new PedestrianSource(new Vector2f(5, 17), 1f, mission, 2)
				.setProduceDelayGenerator(new UniformRandomGenerator(1, 2f))
				.setPedestrianAmountGenerator(new UniformRandomGenerator(1, 1))
		);
		mission.putFirst(new PedestrianTargetArea(new Line(22, 15, 22, 19)));
	}
	
	private void addWalls(PedestrianArea area) {
		// Arriba
		area.addObstacle(new Line(0, 15, 30, 15));
		// Abajo
		area.addObstacle(new Line(0, 19, 30, 19));
	}
}
