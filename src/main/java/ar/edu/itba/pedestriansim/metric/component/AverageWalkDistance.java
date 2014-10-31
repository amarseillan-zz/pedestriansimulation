package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

import com.google.common.collect.Maps;

public class AverageWalkDistance implements SimpleMetric {

	private Map<Integer, Vector2f> lastPositions = Maps.newHashMap();
	private Map<Integer, Float> walkedDistances = Maps.newHashMap();

	public AverageWalkDistance() {
	}

	@Override
	public void onIterationStart() {
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		int id = pedestrianStaticInfo.id();
		Vector2f lastPosition = lastPositions.get(id);
		Float walkedDistance = walkedDistances.get(id);
		Vector2f position = pedestrian.center();
		if (lastPosition == null) {
			lastPositions.put(id, lastPosition = new Vector2f(position));
			walkedDistances.put(id, walkedDistance = 0f);
		}
		walkedDistance += lastPosition.distance(position);
		lastPosition.set(position);
		walkedDistances.put(id, walkedDistance);
	}

	@Override
	public void onIterationEnd() {

	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		float sum = 0;
		for (float f : walkedDistances.values()) {
			sum += f;
		}
		writer.append(String.format("%.3f", sum / walkedDistances.size()));
	}

	@Override
	public String name() {
		return "Prom. Dist. Rec.";
	}

}