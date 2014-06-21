package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;

public class MetricsRunner {

	private static final String staticfileFormatter = "%s-%d.static";
	private static final String dynamicfileFormatter = "%s-%d.dynamic";
	private static final String metricfileFormatter = "%s.metric";

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			throw new IllegalStateException("Too few arguments!");
		}
		Scanner scanner = new Scanner(new File("run.files"));
		while (scanner.hasNextLine()) {
			long start = System.currentTimeMillis();
			String file = null;
			file = scanner.nextLine();
			int stop = Integer.valueOf(args[1]);
			String metricsPath = new File("metrics/") + "" + File.separatorChar + file;
			FileWriter writer = new FileWriter(String.format(metricfileFormatter, metricsPath));
			// XXX: marse no me odies, ni tiempo de ver como funcionaba el tema de las metricas =(
//			for (int i = 0; i<stop; i++) {
//				PedestrianAreaFileSerializer serializer = new PedestrianAreaFileSerializer(null, new File("runs/"), String.format(staticfileFormatter, file, i), String.format(dynamicfileFormatter, file, i));
//				CalculateMetricsFromFile metricRunner = new CalculateMetricsFromFile(serializer.staticFileInfo(), serializer.steps(), writer, false);
//				while (metricRunner.update(serializer.delta()));
//				metricRunner.onSimulationEnd();
//			}
			System.out.println(System.currentTimeMillis() - start);
		}
	}
}
