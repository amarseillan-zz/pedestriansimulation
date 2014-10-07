package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

import com.google.common.collect.Maps;

public class AverageTravelTime implements SimpleMetric {

	private Map<Integer, Float> _travelTimes = Maps.newHashMap();

	@Override
	public void onIterationStart() {
	}

	@Override
	public void onIterationEnd() {
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		Float time = _travelTimes.get(pedestrian.id());
		if (time == null) {
			time = 0f;
		}
		_travelTimes.put(pedestrian.id(), time + delta);
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		float totalTime = 0;
		for (Float time : _travelTimes.values()) {
			totalTime += time;
		}
		writer.append(String.format("%.3f", totalTime / _travelTimes.size()));
	}

	@Override
	public String name() {
		return "T. Prom Viaje";
	}

}