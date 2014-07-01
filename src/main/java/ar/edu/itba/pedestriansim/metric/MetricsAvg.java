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
			List<List<Float>> allValues = Lists.newArrayList();
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] values = line.split(" ");
				List<Float> parsed = Lists.newArrayList();
				for (int i = 0; i < values.length; i++) {
					parsed.add(Float.valueOf(values[i]));
				}
				allValues.add(parsed);
			}
			scanner.close();
			Float[][] result = getAverages(traspose(allValues));
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
	
	private float[][] traspose(List<List<Float>> _matrix) {
		float[][] trasposed = new float[_matrix.get(0).size()][_matrix.size()];
		for (int rowIndex = 0; rowIndex < _matrix.size(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < _matrix.get(0).size(); columnIndex++) {
				
				trasposed[columnIndex][rowIndex] = _matrix.get(rowIndex).get(columnIndex);
			}
		}
		return trasposed;
	}

	private static Float[][] getAverages(float[][] matrix) {
		Float[][] result = new Float[matrix.length][];
		int index = 0;
		for (float[] row : matrix) {
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
