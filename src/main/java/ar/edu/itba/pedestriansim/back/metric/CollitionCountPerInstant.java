package ar.edu.itba.pedestriansim.back.metric;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class CollitionCountPerInstant implements Metric{
	
	long count;
	
	public CollitionCountPerInstant() {
		count = 0;
	}
	
	@Override
	public void onIterationStart() {
		//nothing to do
	}
	
	@Override
	public void update(float miliseconds, Serializable p1, Serializable p2) {
		count ++;
	}
	
	@Override
	public void onIterationEnd() {
		//nothing to do
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		writer.append("Sum of every collition in every instant:\n");
		writer.append(count + "\n");
	}

	public long getCount() {
		return count;
	}

}
