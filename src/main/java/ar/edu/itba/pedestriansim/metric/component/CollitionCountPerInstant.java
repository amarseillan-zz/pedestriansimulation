package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class CollitionCountPerInstant implements CollitionMetric{

	long count;

	public CollitionCountPerInstant() {
		count = 0;
	}

	@Override
	public void onIterationStart() {
		//nothing to do
	}

	@Override
	public void onCollition(float miliseconds, Serializable p1, Serializable p2) {
		count ++;
	}

	@Override
	public void onIterationEnd() {
		//nothing to do
	}

	@Override
	public void appendResults(FileWriter writer, boolean pretty) throws IOException {
		if (pretty) {
			writer.append("Sum of every collition in every instant:\n");
			writer.append(count + "\n");
		} else {
			writer.append(count + " ");
		}
	}

	public long getCount() {
		return count;
	}

}