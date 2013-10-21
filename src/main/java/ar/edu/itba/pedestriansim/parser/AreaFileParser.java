package ar.edu.itba.pedestriansim.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.PedestrianMovementStrategy;
import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.PedestrianSource;

public class AreaFileParser {

	public void load(PedestrianSim simulation, File sourceFile) {
		try {
			Scanner scanner = new Scanner(sourceFile);
			while (scanner.hasNextLine()) {
				String[] values = scanner.nextLine().split(" ");
				if (values[0].equals("rectangle")) {
					float x = Float.parseFloat(values[1]);
					float y = Float.parseFloat(values[2]);
					float width = Float.parseFloat(values[3]);
					float height = Float.parseFloat(values[4]);
					simulation.getScene().addObstacle(new Rectangle(x, y, width, height));
				} else if (values[0].equals("source")) {
					PedestrianMovementStrategy strategy = new PedestrianMovementStrategy();
					float x = Float.parseFloat(values[1]);
					float y = Float.parseFloat(values[2]);
					strategy.append(new Vector2f(Float.parseFloat(values[3]), Float.parseFloat(values[4])));
					if (values.length > 5) {
						strategy.append(new Vector2f(Float.parseFloat(values[5]), Float.parseFloat(values[6])));
					}
					PedestrianSource source = new PedestrianSource(new Vector2f(x, y), strategy, simulation.getScene());
					simulation.getScene().addSource(source);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
}
