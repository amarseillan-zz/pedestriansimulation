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

	public static void main(String[] args) throws IOException {
		final boolean prettyPrint = false;
		float[] thresholds = { 0.0005f, 2f, 4f, 6f, 8f };
		float[] alphas = { 500, 600, 700, 800, 900 };
		float[] betas = { 0.3f, 0.5f, 0.7f, 1f, 1.5f };
		final File runsDirectory = new File("runs");
		runsDirectory.mkdir();
		// FIXME: no usar mas de un thread, la app tira NullPointer sino
		// (porque?? =/)
		ExecutorService executor = Executors.newFixedThreadPool(1);
		for (final float threshold : thresholds) {
			for (final float alpha : alphas) {
				for (final float beta : betas) {
					executor.execute(new Runnable() {
						@Override
						public void run() {
							List<PedestrianAppConfig> runs = Lists.newArrayList();
							for (int runNumber = 0; runNumber < 5; runNumber++) {
								String fileId = buildFileId(threshold, alpha, beta, runNumber);
								System.out.println("Started: " + fileId);
								PedestrianAppConfig config = new CrossingConfig().get();
								config.setStaticfile(new File(runsDirectory + "/" + fileId + "-static.txt"));
								config.setDynamicfile(new File(runsDirectory + "/" + fileId + "dynamic.txt"));
								config.setAlpha(alpha).setBeta(beta).setExternalForceThreshold(threshold);
								new PedestrianSimApp(config).run();
								runs.add(config);
								System.out.println("Finished: " + fileId);
							}
							new MetricsRunner(runs, prettyPrint).run();
						}

						private String buildFileId(float threshold, float alpha, float beta, int runNumber) {
							return "b:" + beta + "-a:" + alpha + "-t:" + threshold + "-c:" + runNumber;
						}
					});
				}
			}
		}
	}
}
