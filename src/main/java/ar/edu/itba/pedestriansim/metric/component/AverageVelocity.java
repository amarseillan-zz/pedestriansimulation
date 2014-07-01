package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;

import ar.edu.itba.common.util.Average;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

public class AverageVelocity implements SimpleMetric{

	Average avgSpeed;

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
	public void appendResults(FileWriter writer, boolean pretty) throws IOException {
		if (pretty) {
			writer.append("Average speed:\n");
			writer.append(avgSpeed.getAverage() + "m/s\n");
		} else {
			writer.append(avgSpeed.getAverage() + " ");
		}
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		avgSpeed.add(pedestrian.velocity().length());
	}

}