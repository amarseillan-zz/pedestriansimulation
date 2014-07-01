package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.Lists;

public class MetricsAvg {

	private final FileWriter _output;

	public MetricsAvg(File output) {
		try {
			output.createNewFile();
			_output = new FileWriter(output);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}


	public void calculate(String name, File file) {
		try {
			List<float[]> allValues = Lists.newArrayList();
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] values = line.split(" ");
				float[] parsed = new float[values.length];
				for (int i = 0; i < values.length; i++) {
					parsed[i] = Float.valueOf(values[i]);
				}
				allValues.add(parsed);
			}
			scanner.close();
			Float[][] result = getAverages(allValues);
			_output.append(name + ":\t");
			for (int i = 0; i < result.length; i++) {
				String s = String.format("(%.3f, %.3f)\t", result[i][0], result[i][1]);
				_output.append(s);
			}
			_output.append("\n");
			_output.flush();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	private static Float[][] getAverages(List<float[]> rows) {
		Float[][] result = new Float[rows.size()][];
		int index = 0;
		for (float[] row : rows) {
			float total = 0f;
			for (int j = 0; j < row.length; j++) {
				total += row[j];
			}
			float mean = total / row.length;
			total = 0;
			for (int j = 0; j < row.length; j++) {
				float aux = (row[j] - mean);
				total += (aux * aux);
			}
			float s = (float) Math.sqrt(total / row.length);
			result[index++] = new Float[] { mean, s };	
		}
		return result;
	}
}
