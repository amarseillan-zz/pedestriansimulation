package ar.edu.itba.pedestriansim.metric.component;


public interface CollitionMetric extends Metric {

	void onCollition(float miliseconds, int p1, int p2);

}
