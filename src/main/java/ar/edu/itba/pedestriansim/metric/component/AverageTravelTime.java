package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.edu.itba.common.util.Average;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

public class AverageTravelTime implements SimpleMetric{

	private Map<Serializable, Float> averageTimePerPedestrian;
	private Set<Serializable> accumulated;
	private Set<Serializable> current;
	private List<Float> result;

	public AverageTravelTime() {
		this.averageTimePerPedestrian = new HashMap<Serializable, Float>();
		this.accumulated = new HashSet<Serializable>();
		this.result = new ArrayList<Float>();
	}

	@Override
	public void onIterationStart() {
		this.current = new HashSet<Serializable>();
	}

	@Override
	public void onIterationEnd() {
		this.accumulated.removeAll(this.current);
		for (Serializable id: this.accumulated) {
			this.result.add(this.averageTimePerPedestrian.get(id));
			this.averageTimePerPedestrian.remove(id);
		}
		this.accumulated = this.current;
	}

	@Override
	public void appendResults(FileWriter writer, boolean pretty) throws IOException {
		Average total = new Average();
		for (Float average: this.result) {
			total.add(average);
		}

		if (pretty) {
			writer.append("Average time to objective: \n");
			writer.append(total.getAverage() + "s\n");
		} else {
			writer.append(total.getAverage() + " ");
		}
	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		Serializable id = pedestrianStaticInfo.id();
		this.current.add(id);
		this.accumulated.add(id);

		if (!averageTimePerPedestrian.containsKey(id)) {
			averageTimePerPedestrian.put(id, 0f);
		}

		Float averageTime = averageTimePerPedestrian.get(id);
		averageTime += delta;
		averageTimePerPedestrian.put(id, averageTime);
	}

}