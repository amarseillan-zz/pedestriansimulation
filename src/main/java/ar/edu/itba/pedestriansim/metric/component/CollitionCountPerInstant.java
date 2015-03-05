package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;

public class CollitionCountPerInstant implements CollitionMetric {

	private long count;

	public CollitionCountPerInstant() {
		count = 0;
	}

	@Override
	public void onIterationStart() {
		// nothing to do
	}

	@Override
	public void onCollition(float miliseconds, int p1, int p2) {
		count++;
	}

	@Override
	public void onIterationEnd() {
		// nothing to do
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		writer.append(String.format("%d", count));
	}

	public long getCount() {
		return count;
	}

	@Override
	public String name() {
		return "Coll x inst.";
	}
}