package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;

import ar.edu.itba.common.util.Average;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

public class AverageVelocity implements SimpleMetric {

	private Average avgSpeed;

	public AverageVelocity() {
		avgSpeed = new Average();
	}

	@Override
	public void onIterationStart() {
	}

	@Override
	public void onIterationEnd() {
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		avgSpeed.add(pedestrian.velocity().length());
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		writer.append(String.format("%.3f", avgSpeed.getAverage()));
	}

	@Override
	public String name() {
		return "Vel. prom";
	}

}