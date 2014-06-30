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

	private static final String staticfileFormatter = "%s-%d.static";
	private static final String dynamicfileFormatter = "%s-%d.dynamic";
	private static final String metricfileFormatter = "%s.metric";

	private static final File _metricsDirectory;
	static {
		_metricsDirectory = new File("metrics/");
		_metricsDirectory.mkdir();
	}
	
	private final List<PedestrianAppConfig> _runs;
	private final boolean _prettyPrint;

	public MetricsRunner(List<PedestrianAppConfig> runs, boolean prettyPrint) {
		_runs = Preconditions.checkNotNull(runs);
		_prettyPrint = prettyPrint;
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			for (PedestrianAppConfig config : _runs) {
				final Closer closer = Closer.create();
				FileWriter writer = closer.register(writer(config.staticfile() + ".metric"));
				PedestrianAreaFileSerializer serializer = new PedestrianAreaFileSerializer();
				Supplier<StaticFileLine> staticInfo = serializer.staticFileInfo(closer.register(new Scanner(config.staticfile())));
				Supplier<DymaimcFileStep> steps = serializer.steps(closer.register(new Scanner(config.dynamicfile())));
				float timeStep = config.pedestrianArea().timeStep().floatValue();
				new CalculateMetricsFromFile(staticInfo, steps, writer, _prettyPrint)
					.runMetrics(timeStep);
				closer.close();
			}
			System.out.println(System.currentTimeMillis() - start);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private FileWriter writer(String name) throws IOException {
		String metricsPath = _metricsDirectory.getPath() + File.separatorChar + name;
		return new FileWriter(String.format(metricfileFormatter, metricsPath));
	}
}
