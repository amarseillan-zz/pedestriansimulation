package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ar.edu.itba.pedestriansim.back.PedestrianSimApp;
import ar.edu.itba.pedestriansim.back.config.CrossingConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;

public class RunsGenerator {

	public static void main(String[] args) throws IOException {
		final boolean prettyPrint = false;
		float[] thresholds = { 0.0005f, 2f, 4f, 6f, 8f };
		float[] alphas = { 100f, 125f, 150f, 175f, 200f };
		float[] betas = { 0.5f, 1f, 1.5f, 2f };
		final File runsDirectory = new File("runs");
		runsDirectory.mkdir();
		// FIXME: no usar mas de un thread, la app tira NullPointer sino (porque?? =/)
		ExecutorService executor = Executors.newFixedThreadPool(1);
		for (final float threshold : thresholds) {
			for (final float alpha : alphas) {
				for (final float beta : betas) {
					executor.execute(new Runnable() {
						@Override
						public void run() {
							String fileId = buildFileId(threshold, alpha, beta);
							System.out.println("Started: " + fileId);
							PedestrianAppConfig config = new CrossingConfig().get();
							config.setStaticfile(new File(runsDirectory + "/" + fileId + "-static.txt"));
							config.setDynamicfile(new File(runsDirectory + "/" + fileId + "dynamic.txt"));
							config.setAlpha(alpha).setBeta(beta).setExternalForceThreshold(threshold);
							new PedestrianSimApp(config).run();
							new MetricsRunner(config, prettyPrint).run();
							System.out.println("Finished: " + fileId);
						}
						
						private String buildFileId(float threshold, float alpha, float beta) {
							return "b:" + beta + "-a:" + alpha + "-t:" + threshold;
						}
					});
				}
			}
		}
	}
}
