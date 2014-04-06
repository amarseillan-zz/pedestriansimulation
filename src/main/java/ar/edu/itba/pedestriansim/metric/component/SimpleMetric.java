package ar.edu.itba.pedestriansim.metric.component;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

public interface SimpleMetric extends Metric{

	void update(PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo);
}
