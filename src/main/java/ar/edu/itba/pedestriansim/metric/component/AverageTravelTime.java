package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.common.util.Average;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

public class AverageTravelTime implements SimpleMetric{

	Map<Serializable, Float> averageTimePerPedestrian;
	
	public AverageTravelTime() {
		this.averageTimePerPedestrian = new HashMap<Serializable, Float>();
	}
	
	@Override
	public void onIterationStart() {
		
	}

	@Override
	public void onIterationEnd() {
		
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		Average total = new Average();
		for (Float average: averageTimePerPedestrian.values()) {
			total.add(average);
		}
		
		writer.append("Average time to objective: \n");
		writer.append(total.getAverage() + "s\n");
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		Serializable id = pedestrianStaticInfo.id();
		
		if (!averageTimePerPedestrian.containsKey(id)) {
			averageTimePerPedestrian.put(id, 0f);
		}
		
		Float averageTime = averageTimePerPedestrian.get(id);
		averageTime += delta;
		averageTimePerPedestrian.put(id, averageTime);
	}

}
