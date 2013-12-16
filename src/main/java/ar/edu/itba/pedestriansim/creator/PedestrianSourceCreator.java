package ar.edu.itba.pedestriansim.creator;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.GaussianRandomGenerator;
import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianFactory;
import ar.edu.itba.pedestriansim.back.PedestrianSource;
import ar.edu.itba.pedestriansim.back.PedestrianTargetArea;
import ar.edu.itba.pedestriansim.back.PedestrianTargetList;

public class PedestrianSourceCreator {

	private PedestrianArea _pedestrianArea;
	
	public PedestrianSourceCreator(PedestrianArea pedestrianArea) {
		_pedestrianArea = pedestrianArea;
	}
	
	public PedestrianSource produce(Properties properties, PedestrianFactory pedestrianFactory) {
		int team = Integer.parseInt(properties.getProperty("team"));
		float x = Float.valueOf(properties.getProperty("x"));
		float y = Float.valueOf(properties.getProperty("y"));
		float radius = Float.valueOf(properties.getProperty("radius"));
		Vector2f location = new Vector2f(x, y);
		PedestrianSource source = new PedestrianSource(pedestrianFactory, location, radius, parsePedestrianTarget(properties), _pedestrianArea, team);
		String limitString = properties.getProperty("produce.limit");
		if (!StringUtils.isEmpty(limitString)) {
			source.setProduceLimit(Integer.valueOf(limitString));
		}
		source.setProduceDelayGenerator(parseRandomDistribution(properties.getProperty("produce.delay").split(" ")));
		source.setPedestrianAmountGenerator(parseRandomDistribution(properties.getProperty("produce.amount").split(" ")));
		return source;
	}
	
	private PedestrianTargetList parsePedestrianTarget(Properties properties) {
		PedestrianTargetList targetList = new PedestrianTargetList();
		int targets = Integer.parseInt(properties.getProperty("target"));
		PedestrianTargetArea current = new PedestrianTargetArea(readLine(properties.getProperty("target.1").split(" ")));
		targetList.putFirst(current);
		for (int i = 2; i <= targets; i++) {
			PedestrianTargetArea next = new PedestrianTargetArea(readLine(properties.getProperty("target." + i).split(" ")));
			targetList.put(current, next);
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
