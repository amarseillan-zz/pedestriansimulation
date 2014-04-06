package ar.edu.itba.pedestriansim.metric.component;

import java.io.Serializable;

public interface CollitionMetric extends Metric{
	
	void onCollition(float miliseconds, Serializable p1, Serializable p2);
	
}
