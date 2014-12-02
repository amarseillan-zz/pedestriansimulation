package ar.edu.itba.pedestriansim.back.config;

import org.apache.commons.lang3.tuple.Pair;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import com.google.common.collect.Range;

import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;
import ar.edu.itba.pedestriansim.back.entity.Wall;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianTargetArea;

public class SquareRoomConfig implements ApplicationConfigBuilder {

	private final float xOffset = 1;
	private final float yOffset = xOffset;
	private final float WALL_WIDTH = 20;
	private final float DOOR_WIDTH = 1.5f;
	
	private final float delay = 0.2f;

	// Helper variables
	private float maxX = xOffset + WALL_WIDTH;
	private float maxY = yOffset + WALL_WIDTH;
	private final float doorXMin = xOffset + (WALL_WIDTH - DOOR_WIDTH) / 2;
	private final float doorXMax = doorXMin + DOOR_WIDTH;

	private ApplicationConfigBuilder _defaultBuilder;
	
	public SquareRoomConfig() {
		this(new DefaultPedestrianAppConfig());
	}

	public SquareRoomConfig(ApplicationConfigBuilder defaultBuilder) {
		_defaultBuilder = defaultBuilder;
	}

	@Override
	public PedestrianAppConfig get() {
		PedestrianAppConfig config = _defaultBuilder.get();
		config.pedestrianFactory().setPedestrianAlphaBeta(Pair.of(1000f, Range.closed(0.3f, 0.4f)));
		addWalls(config.pedestrianArea());
		addSource1(config.pedestrianArea());
		return config;
	}

	private void addSource1(PedestrianArea area) {
		PedestrianMision mission = new PedestrianMision();
		final float halfWallWidth = WALL_WIDTH / 2;
		area.addSource(
			new PedestrianSource(new Vector2f(xOffset + halfWallWidth, yOffset + halfWallWidth), halfWallWidth * 0.8f, mission, 1)
				.setProduceDelayGenerator(new UniformRandomGenerator(delay, delay))
				.setPedestrianAmountGenerator(new UniformRandomGenerator(1, 1))
				.setProduceLimit(100)
		);
		PedestrianTargetArea first; 
		mission.putFirst(first = new PedestrianTargetArea(new Line(doorXMin + 1.1f, maxY + 0.0f, doorXMax - 1.1f, maxY + 0.0f)).setId(1));
		mission.putTransition(first, new PedestrianTargetArea(
			new Line(doorXMin, maxY + 4, doorXMax, maxY + 4)
		).setId(2));
	}

	private void addWalls(PedestrianArea area) {
		// Arriba
		area.addObstacle(new Wall(new Line(xOffset, yOffset, maxX, yOffset)));
		// izquierda
		area.addObstacle(new Wall(new Line(xOffset, yOffset, xOffset, maxY)));
		// derecha
		area.addObstacle(new Wall(new Line(maxX, yOffset, maxX, maxY)));
		// Abajo
		area.addObstacle(new Wall(new Line(xOffset, maxY, doorXMin, maxY))
			.setThickDirection(new Vector2f(0, -1))
		);
		area.addObstacle(new Wall(new Line(doorXMax, maxY, maxX, maxY))
			.setThickDirection(new Vector2f(0, -1))
		);
		// Pasillo al final
		area.addObstacle(new Wall(new Line(doorXMin, maxY, doorXMin, maxY + 10))
			.setThickDirection(new Vector2f(1, 0))
		);
		area.addObstacle(new Wall(new Line(doorXMax, maxY, doorXMax, maxY + 10))
			.setThickDirection(new Vector2f(-1, 0))
		);
	}
}
