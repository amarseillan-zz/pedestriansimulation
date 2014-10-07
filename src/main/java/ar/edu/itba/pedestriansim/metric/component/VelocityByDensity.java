package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import ar.edu.itba.common.util.Average;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

import com.google.common.collect.Lists;

public class VelocityByDensity implements SimpleMetric {

	private List<Pair<Long, Float>> velocitiesByDensities;
	private Long count;
	private Average velocity;

	public VelocityByDensity() {
		this.velocitiesByDensities = Lists.newArrayList();
		this.count = 0L;
		this.velocity = new Average();

	}

	@Override
	public void onIterationStart() {
		count = 0L;
		velocity = new Average();
	}

	@Override
	public void onIterationEnd() {
		velocitiesByDensities.add(new ImmutablePair<Long, Float>(count, velocity.getAverage()));
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		for (Pair<Long, Float> p : velocitiesByDensities) {
			writer.append(p.getLeft() + "\t" + p.getRight() + "\n");
		}
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		count++;
		velocity.add(pedestrian.velocity().length());
	}

	public List<Pair<Long, Float>> getVBDs() {
		return velocitiesByDensities;
	}

	@Override
	public String name() {
		return "Velocidad por densidad";
	}
}
