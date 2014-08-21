package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CollitionCount implements CollitionMetric {

	long count;
	Map<Serializable, Serializable> lastCollitions;
	Map<Serializable, Serializable> thisCollitions;

	public CollitionCount() {
		count = 0;
		lastCollitions = new HashMap<Serializable, Serializable>();
		thisCollitions = new HashMap<Serializable, Serializable>();
	}

	@Override
	public void onIterationStart() {
		lastCollitions.clear();
		lastCollitions.putAll(thisCollitions);
		thisCollitions.clear();
	}

	@Override
	public void onCollition(float miliseconds, Serializable p1, Serializable p2) {
		Serializable candidate = lastCollitions.get(p1);
		if (candidate == null || !candidate.equals(p2)) {
			count++;
		}
		thisCollitions.put(p1, p2);
		thisCollitions.put(p2, p1);
	}

	@Override
	public void onIterationEnd() {
		//nothing to do
	}

	@Override
	public void appendResults(FileWriter writer, boolean pretty) throws IOException {
		if (pretty) {
			writer.append("Number of collitions:\n");
			writer.append(count + "\n");
		} else {
			writer.append(count + " ");
		}
	}


	public long getCount() {
		return this.count;
	}

}