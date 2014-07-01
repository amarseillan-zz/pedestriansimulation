package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.common.base.Preconditions;

public class MetricsAvg {

	private File _file;
	private int _numberOfRuns;

	public MetricsAvg(File file, int numberOfRuns) {
		_file = Preconditions.checkNotNull(file);
		_numberOfRuns = numberOfRuns;
	}

	public void calculate() throws IOException {
		Float[][] allValues = new Float[_numberOfRuns][];
		for (int i = 0; i < _numberOfRuns; i++) {
			allValues[i] = new Float[5];
		}
		FileWriter writer = new FileWriter("avgMetrics.out", true);
		Scanner scanner = new Scanner(_file);
		String name = scanner.nextLine();
		int j = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			String[] values = line.split(" ");
			for (int i = 0; i < values.length; i++) {
				allValues[j][i] = Float.valueOf(values[i]);
			}
			j++;
		}
		scanner.close();
		Float[][] result = getAverages(allValues);
		writer.append(name + ":\t");
		for (int i = 0; i < result.length; i++) {
			String s = String.format("(%.3f, %.3f)\t", result[i][0], result[i][1]);
			writer.append(s);
		}
		writer.append("\n");
		writer.close();
	}

	private static Float[][] getAverages(Float[][] values) {
		Float[][] result = new Float[values.length][];
		for (int i = 0; i < values.length; i++) {
			float total = 0f;
			for (int j = 0; j < values[i].length; j++) {
				total += values[i][j];
			}
			float mean = total / values[i].length;
			total = 0;
			for (int j = 0; j < values[i].length; j++) {
				float aux = (values[i][j] - mean);
				total += (aux * aux);
			}
			float s = (float) Math.sqrt(total / values[i].length);
			result[i] = new Float[] { mean, s };
		}
		return result;
	}
}
