package ar.edu.itba.pedestriansim.batch;

import java.io.FileWriter;
import java.io.IOException;

import ar.edu.itba.pedestriansim.back.PedestrianSimApp;

public class RunsGenerator {

	private static final Integer STATIC = 0;
	private static final Integer DYNAMIC = 1;
	private static final Integer THRESHOLD = 2;
	private static final Integer ALPHA = 3;
	private static final Integer BETA = 4;
	private static final String staticFormatter = "runs/%s-%s-%s-%s.static";
	private static final String dynamicFormatter = "runs/%s-%s-%s-%s.dynamic";

	public static void main(String[] args) throws IOException {
		String[] thresholds = { "0.0005", "2", "4", "6", "8"};
		String[] alphas = { "100", "125", "150", "175", "200" };
		String[] betas = { "0.5", "1", "1.5", "2" };

		String[] newArgs = new String[5];
		FileWriter writer = new FileWriter("run.files");
		for (int i = 0; i < thresholds.length; i++) {
			for (int j = 0; j < alphas.length; j++) {
				for (int k = 0; k < betas.length; k++) {
					newArgs[THRESHOLD] = thresholds[i];
					newArgs[ALPHA] = alphas[j];
					newArgs[BETA] = betas[k];
					String file = String.format("%s-%s-%s\n", thresholds[i], alphas[j], betas[k]);
					System.out.println(file);
					writer.append(file);
					for (int l = 0; l < 5; l++) {
						newArgs[STATIC] = String.format(staticFormatter, newArgs[THRESHOLD], newArgs[ALPHA], newArgs[BETA], l + "");
						newArgs[DYNAMIC] = String.format(dynamicFormatter, newArgs[THRESHOLD], newArgs[ALPHA], newArgs[BETA], l + "");
						System.out.println();
						PedestrianSimApp.main(newArgs);
					}
				}
			}
		}
		writer.flush();
		writer.close();
	}
}
