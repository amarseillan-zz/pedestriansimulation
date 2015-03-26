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
		if (lastPosition == null) {
			walkedDistances.put(id, 0f);
		} else {
			Float walkedDistance = walkedDistances.get(id);
			float deltaX = lastPosition.distance(pedestrian.center());
			walkedDistances.put(id, walkedDistance + deltaX);
		}
		lastPositions.put(id, pedestrian.center().copy());
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