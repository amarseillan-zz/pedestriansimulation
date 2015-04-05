package ar.edu.itba.pedestriansim.back.config;

import java.io.File;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

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

	private final float delay = 0.2f;
	private float doorWidth = 1.5f;
	private int amount = 100;

	// Helper variables
	private float maxX = xOffset + WALL_WIDTH;
	private float maxY = yOffset + WALL_WIDTH;

	private ApplicationConfigBuilder _defaultBuilder;
	
	public SquareRoomConfig() {
		this(new DefaultPedestrianAppConfig());
	}

	public SquareRoomConfig(ApplicationConfigBuilder defaultBuilder) {
		_defaultBuilder = defaultBuilder;
	}

	public void setDoorWidth(float doorWidth) {
		this.doorWidth = doorWidth;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public PedestrianAppConfig get() {
		PedestrianAppConfig config = _defaultBuilder.get();
		config.setStaticfile(new File(config.outDir() + "static-" + amount + "-" + doorWidth + ".txt"));
		config.setDynamicfile(new File(config.outDir() + "dynamic-" + amount + "-" + doorWidth + ".txt"));
		addWalls(config.pedestrianArea());
		addSource1(config.pedestrianArea());
		return config;
	}
	
	private float doorXMin() {
		return xOffset + (WALL_WIDTH - doorWidth) / 2;
	}

	private float doorXMax() {
		return doorXMin() + doorWidth;
	}

	private void addSource1(PedestrianArea area) {
		PedestrianMision mission = new PedestrianMision();
		final float halfWallWidth = WALL_WIDTH / 2;
		area.addSource(
			new PedestrianSource(new Vector2f(xOffset + halfWallWidth, yOffset + halfWallWidth), halfWallWidth * 0.8f, mission, 1)
				.setProduceDelayGenerator(new UniformRandomGenerator(delay, delay))
				.setPedestrianAmountGenerator(new UniformRandomGenerator(1, 1))
				.setProduceLimit(amount)
		);
		PedestrianTargetArea first; 
		mission.putFirst(first = new PedestrianTargetArea(new Line(doorXMin() + 1.1f, maxY + 0.0f, doorXMax() - 1.1f, maxY + 0.0f)).setId(1));
		mission.putTransition(first, new PedestrianTargetArea(
			new Line(doorXMin(), maxY + 4, doorXMax(), maxY + 4)
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
		area.addObstacle(new Wall(new Line(xOffset, maxY, doorXMin(), maxY))
			.setThickDirection(new Vector2f(0, -1))
		);
		area.addObstacle(new Wall(new Line(doorXMax(), maxY, maxX, maxY))
			.setThickDirection(new Vector2f(0, -1))
		);
		// Pasillo al final
		area.addObstacle(new Wall(new Line(doorXMin(), maxY, doorXMin(), maxY + 10))
			.setThickDirection(new Vector2f(1, 0))
		);
		area.addObstacle(new Wall(new Line(doorXMax(), maxY, doorXMax(), maxY + 10))
			.setThickDirection(new Vector2f(-1, 0))
		);
	}
}
