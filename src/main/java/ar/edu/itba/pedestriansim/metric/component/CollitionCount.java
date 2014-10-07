package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;

public class CollitionCount implements CollitionMetric {

	private long count = 0;
	private Set<CollitionPair> lastCollitions = Sets.newHashSet();
	private Set<CollitionPair> currentCollitions = Sets.newHashSet();

	@Override
	public void onIterationStart() {
		lastCollitions.clear();
		lastCollitions.addAll(currentCollitions);
		currentCollitions.clear();
	}

	@Override
	public void onCollition(float miliseconds, int p1, int p2) {
		CollitionPair collitionPair = new CollitionPair(p1, p2);
		boolean alreadyColliding = lastCollitions.contains(collitionPair);
		if (!alreadyColliding) {
			count++;
		}
		currentCollitions.add(collitionPair);
	}

	@Override
	public void onIterationEnd() {
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
		return "Cant. Colis.";
	}

	private static class CollitionPair {
		private final int _p1Id, _p2Id;

		public CollitionPair(int p1Id, int p2Id) {
			_p1Id = p1Id;
			_p2Id = p2Id;
		}

		@Override
		public int hashCode() {

			return Integer.hashCode(_p1Id) + Integer.hashCode(_p2Id);
		}

		@Override
		public boolean equals(Object obj) {
			CollitionPair other = (CollitionPair) obj;
			return _p1Id == other._p1Id && _p2Id == other._p2Id;
		}
	}
}