package ar.edu.itba.pedestriansim.metric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MetricsAvg {

	
	public static void main(String[] args) throws IOException {
		Scanner scanner = null;
		Float[][] allValues = new Float[5][];
		FileWriter writer = new FileWriter("avgMetrics.out");
		for (int i=0; i<5; i++) {
			allValues[i] = new Float[5];
		}
		try {
		scanner = new Scanner(new File("run.files"));
		} catch (FileNotFoundException e) {
			System.out.println("Run your scripts first yo!");
			writer.close();
			return;
		}
		while (scanner.hasNextLine()) {
			int j = 0;
			String file = scanner.nextLine();
			Scanner otherScanner = new Scanner(new File("metrics/" + "" + File.separatorChar + file + ".metric"));
			while (otherScanner.hasNextLine()) {
				String line = otherScanner.nextLine().trim();
				String[] values = line.split(" ");
				for (int i=0; i<values.length; i++) {
					allValues[i][j] = Float.valueOf(values[i]);
				}
				j++;
			}
			otherScanner.close();
			Float[][] result = getAverages(allValues);
			if (file.length() == 7) {
				file += "  ";
			}
			writer.append(file + ":\t");
			for (int i=0; i<result.length; i++) {
				writer.append(String.format("(%.3f, %.3f)\t", result[i][0], result[i][1]));
			}
			writer.append("\n");
		}
		writer.flush();
		scanner.close();
		writer.close();
	}
	
	
	private static Float[][] getAverages(Float[][] values) {
		Float[][] result = new Float[5][];
		for (int i=0; i<values.length; i++) {
			float total = 0f;
			for (int j=0; j<values[i].length; j++) {
				total += values[i][j];
			}
			float mean = total / values[i].length;
			total = 0;
			for (int j=0; j<values[i].length; j++) {
				float aux = (values[i][j] - mean);
				total += (aux*aux);
			}
			float s = (float)Math.sqrt(total / values[i].length);
			result[i] = new Float[] {mean, s};
		}
		return result;
	}
}
