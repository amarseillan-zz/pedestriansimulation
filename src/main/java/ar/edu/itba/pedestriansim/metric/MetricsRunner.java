package ar.edu.itba.pedestriansim.metric;

import java.io.File;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;

public class MetricsRunner {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			throw new IllegalStateException("Too few arguments!");
		}
		PedestrianAreaFileSerializer serializer = new PedestrianAreaFileSerializer(null, new File(args[0]));
		CalculateMetricsFromFile metricRunner = new CalculateMetricsFromFile(serializer.staticFileInfo(), serializer.steps(), "metrics.out");
		while (metricRunner.update(serializer.delta()));
		metricRunner.onSimulationEnd();
	}
}
