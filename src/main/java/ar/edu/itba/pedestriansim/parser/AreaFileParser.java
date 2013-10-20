package ar.edu.itba.pedestriansim.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.geom.Rectangle;

import ar.edu.itba.pedestriansim.back.PedestrianSim;

public class AreaFileParser {

	public void load(PedestrianSim simulation, File source) {
		try {
			Scanner scanner = new Scanner(source);
			while (scanner.hasNextLine()) {
				String[] values = scanner.nextLine().split(" ");
				if (values[0].equals("rectangle")) {
					float x = Float.parseFloat(values[1]);
					float y = Float.parseFloat(values[2]);
					float width = Float.parseFloat(values[3]);
					float height = Float.parseFloat(values[4]);
					simulation.getScene().addObstacle(new Rectangle(x, y, width, height));
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
}
