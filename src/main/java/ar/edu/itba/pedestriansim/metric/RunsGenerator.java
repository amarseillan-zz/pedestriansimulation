package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.tuple.Pair;

import ar.edu.itba.command.CommandParam;
import ar.edu.itba.command.CommandParser;
import ar.edu.itba.command.ParsedCommand;
import ar.edu.itba.pedestriansim.back.PedestrianSimApp;
import ar.edu.itba.pedestriansim.back.config.CrossingConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

@SuppressWarnings("serial")
public class RunsGenerator {

	public static final CommandParser parser;
	static {
		parser = new CommandParser()
			.param(new CommandParam("-newRun").required().constrained("true", "false").message("Crear nueva corrida?"))
			.param(new CommandParam("-metricsDir").defaultValue("metrics").message("Directorio a utilizar para guardar las metricas"))
		;
	}

	private static final int RUNS_COUNT = 5;

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println(parser.getHelp());
			return;
		}
		ParsedCommand cmd = parser.parse(args);
		if (cmd.hasErrors()) {
			System.out.println(cmd.getErrorString());
			return;
		}
		boolean newRun = "true".equals(cmd.param("-newRun"));
		String metricsDir = cmd.param("-metricsDir");
		new RunsGenerator(newRun, metricsDir).generate();
	}

	private final float[] thresholds = { 0f };
	private final float[] alphas = { 800 };
	private List<Range<Float>> betas = new LinkedList<Range<Float>>() {{
		add(Range.closed(0.65f, 0.85f));
	}};

	private final File _metricsDirectory;
	private final File runsDirectory;
	private boolean _newRun;

	public RunsGenerator(boolean newRun, String metricsDir) {
		_newRun = newRun;
		_metricsDirectory = new File(metricsDir);
		runsDirectory = new File(_metricsDirectory + File.separator + "runs");
		_metricsDirectory.mkdir();
		runsDirectory.mkdir();
	}

	public void generate() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final MetricsAvg avg = new MetricsAvg(new File(_metricsDirectory + File.separator + "avg.txt"));
		for (final float threshold : thresholds) {
			for (final float alpha : alphas) {
				for (final Range<Float> beta : betas) {
					executor.execute(new Runnable() {
						@Override
						public void run() {
							String id = buildFileId(threshold, alpha, beta);
							List<PedestrianAppConfig> runs = runSimulations(id, alpha, beta, threshold);
							File output = new File(_metricsDirectory + File.separator + id + ".txt");
							new MetricsRunner(output, runs).run();
							avg.calculate(id, output);
						}

						private String buildFileId(float threshold, float alpha, Range<Float> beta) {
							return "b=" + beta.lowerEndpoint() + "-a=" + alpha + "-t=" + threshold;
						}
					});
				}
			}
		}

		
	}

	private List<PedestrianAppConfig> runSimulations(String id, float alpha, Range<Float> beta, float threshold) {
		List<PedestrianAppConfig> runs = Lists.newArrayList();
		for (int runNumber = 0; runNumber < RUNS_COUNT; runNumber++) {
			String fileId = id + "-c=" + runNumber;
			System.out.println("Started: " + fileId);
			PedestrianAppConfig config = new CrossingConfig().get();
			config.setStaticfile(new File(runsDirectory + File.separator + fileId + "-static.txt"));
			config.setDynamicfile(new File(runsDirectory + File.separator + fileId + "-dynamic.txt"));
			config.pedestrianFactory().setPedestrianAlphaBeta(Pair.of(alpha, beta));
			config.setExternalForceThreshold(threshold);
			if (_newRun) {
				new PedestrianSimApp(config).run();
			}
			runs.add(config);
			System.out.println("Finished: " + fileId);
		}
		return runs;
	}
}
