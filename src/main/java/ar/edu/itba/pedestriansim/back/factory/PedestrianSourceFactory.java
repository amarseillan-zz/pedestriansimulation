package ar.edu.itba.pedestriansim.back.factory;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.GaussianRandomGenerator;
import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;
import ar.edu.itba.pedestriansim.back.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.mision.PedestrianTargetArea;

public class PedestrianSourceFactory {

	private PedestrianArea _pedestrianArea;
	
	public PedestrianSourceFactory(PedestrianArea pedestrianArea) {
		_pedestrianArea = pedestrianArea;
	}
	
	public PedestrianSource produce(Properties pedestrianSourceConfig, PedestrianFactory pedestrianFactory) {
		int team = Integer.parseInt(pedestrianSourceConfig.getProperty("team"));
		float x = Float.valueOf(pedestrianSourceConfig.getProperty("x"));
		float y = Float.valueOf(pedestrianSourceConfig.getProperty("y"));
		float radius = Float.valueOf(pedestrianSourceConfig.getProperty("radius"));
		Vector2f location = new Vector2f(x, y);
		PedestrianSource source = new PedestrianSource(pedestrianFactory, location, radius, parsePedestrianTarget(pedestrianSourceConfig), _pedestrianArea, team);
		String limitString = pedestrianSourceConfig.getProperty("produce.limit");
		if (!StringUtils.isEmpty(limitString)) {
			source.setProduceLimit(Integer.valueOf(limitString));
		}
		source.setProduceDelayGenerator(parseRandomDistribution(pedestrianSourceConfig.getProperty("produce.delay").split(" ")));
		source.setPedestrianAmountGenerator(parseRandomDistribution(pedestrianSourceConfig.getProperty("produce.amount").split(" ")));
		return source;
	}
	
	private PedestrianMision parsePedestrianTarget(Properties properties) {
		PedestrianMision targetList = new PedestrianMision();
		int targets = Integer.parseInt(properties.getProperty("target"));
		PedestrianTargetArea current = new PedestrianTargetArea(readLine(properties.getProperty("target.1").split(" ")));
		targetList.putFirst(current);
		for (int i = 2; i <= targets; i++) {
			PedestrianTargetArea next = new PedestrianTargetArea(readLine(properties.getProperty("target." + i).split(" ")));
			targetList.putTransition(current, next);
			current = next;
		}
		return targetList;
	}
	
	private Line readLine(String[] values) {
		float x1 = Float.parseFloat(values[0]);
		float y1 = Float.parseFloat(values[1]);
		float x2 = Float.parseFloat(values[2]);
		float y2 = Float.parseFloat(values[3]);
		return new Line(x1, y1, x2, y2);
	}
	
	private RandomGenerator parseRandomDistribution(String[] values) {
		switch (values[0]) {
		case "gauss":
			return new GaussianRandomGenerator(Float.valueOf(values[1]), Float.valueOf(values[2]));
		case "uniform":
			return new UniformRandomGenerator(Float.valueOf(values[1]), Float.valueOf(values[2]));
		default:
			break;
		}
		throw new IllegalStateException("Unknown type " + values[0]);
	}
}
