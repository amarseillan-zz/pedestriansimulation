package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ar.edu.itba.pedestriansim.back.PedestrianSimApp;
import ar.edu.itba.pedestriansim.back.config.CrossingConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;

import com.google.common.collect.Lists;

public class RunsGenerator {

	private static final File _metricsDirectory;
	private static final int RUNS_COUNT = 1;
	static {
		_metricsDirectory = new File("metrics/");
		_metricsDirectory.mkdir();
	}

	public static void main(String[] args) throws IOException {
		new RunsGenerator().generate();
	}

	private final boolean prettyPrint = false;
	private final float[] thresholds = { 0.01f, 0.01f, 1f, 10f };
	private final float[] alphas = { 10, 100, 1000, 10000 };
	private final float[] betas = { 0.1f, 1f, 10f, 100f };

	private final File runsDirectory = new File("runs");

	public RunsGenerator() {
		runsDirectory.mkdir();
	}
	
	public void generate() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final MetricsAvg avg = new MetricsAvg(new File("avg.txt"));
		for (final float threshold : thresholds) {
			for (final float alpha : alphas) {
				for (final float beta : betas) {
					executor.execute(new Runnable() {
						@Override
						public void run() {
							String id = buildFileId(threshold, alpha, beta);
							List<PedestrianAppConfig> runs = runSimulations(id, alpha, beta, threshold);
							File output = new File(_metricsDirectory + "/" + id + ".txt");
							new MetricsRunner(output, runs, prettyPrint).run();
							avg.calculate(id, output);
						}

						private String buildFileId(float threshold, float alpha, float beta) {
							return "b:" + beta + "-a:" + alpha + "-t:" + threshold;
						}
					});
				}
			}
		}
		
	}

	private List<PedestrianAppConfig> runSimulations(String id, float alpha, float beta, float threshold) {
		List<PedestrianAppConfig> runs = Lists.newArrayList();
		for (int runNumber = 0; runNumber < RUNS_COUNT; runNumber++) {
			String fileId = id + "-c:" + runNumber;
			System.out.println("Started: " + fileId);
			PedestrianAppConfig config = new CrossingConfig().get();
			config.setStaticfile(new File(runsDirectory + "/" + fileId + "-static.txt"));
			config.setDynamicfile(new File(runsDirectory + "/" + fileId + "-dynamic.txt"));
			config.setAlpha(alpha).setBeta(beta).setExternalForceThreshold(threshold);
			new PedestrianSimApp(config).run();
			runs.add(config);
			System.out.println("Finished: " + fileId);
		}
		return runs;
	}
}
