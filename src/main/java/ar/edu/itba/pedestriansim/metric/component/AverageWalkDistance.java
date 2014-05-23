package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.common.util.Average;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

public class AverageWalkDistance implements SimpleMetric{

	Map<Serializable, Float> averageWalkPerPedestrian;
	
	public AverageWalkDistance() {
		this.averageWalkPerPedestrian = new HashMap<Serializable, Float>();
	}
	
	@Override
	public void onIterationStart() {
		
	}

	@Override
	public void onIterationEnd() {
		
	}

	@Override
	public void appendResults(FileWriter writer, boolean pretty) throws IOException {
		Average total = new Average();
		for (Float average: averageWalkPerPedestrian.values()) {
			total.add(average);
		}
		
		if (pretty) {
			writer.append("Average distance to objective: \n");
			writer.append(total.getAverage() + "m\n");
		} else {
			writer.append(total.getAverage() + " ");
		}
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		Serializable id = pedestrianStaticInfo.id();
		
		if (!averageWalkPerPedestrian.containsKey(id)) {
			averageWalkPerPedestrian.put(id, 0f);
		}
		
		Float averageTime = averageWalkPerPedestrian.get(id);
		averageTime += delta * pedestrian.velocity().length();
		averageWalkPerPedestrian.put(id, averageTime);
	}

}
