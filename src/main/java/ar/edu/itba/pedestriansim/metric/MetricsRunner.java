package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.io.Closer;

public class MetricsRunner implements Runnable {

	private final File _output;
	private final List<PedestrianAppConfig> _runs;

	public MetricsRunner(File output, List<PedestrianAppConfig> runs) {
		_output = Preconditions.checkNotNull(output);
		_runs = Preconditions.checkNotNull(runs);
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			FileWriter writer = new FileWriter(_output);
			boolean isFirstTime = true;
			for (PedestrianAppConfig config : _runs) {
				Closer closer = Closer.create();
				PedestrianAreaFileSerializer serializer = new PedestrianAreaFileSerializer();
				Supplier<StaticFileLine> staticInfo = serializer.staticFileInfo(closer.register(new Scanner(config.staticfile())));
				Supplier<DymaimcFileStep> steps = serializer.steps(closer.register(new Scanner(config.dynamicfile())));
				float timeStep = config.pedestrianArea().timeStep().floatValue();
				new CalculateMetricsFromFile(staticInfo, steps, writer).appendHeaderIf(isFirstTime).runMetrics(timeStep);
				isFirstTime = false;
				closer.close();
			}
			writer.close();
			System.out.println(System.currentTimeMillis() - start);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
