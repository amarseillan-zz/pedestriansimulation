package ar.edu.itba.pedestriansim.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.PedestrianMovementStrategy;
import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.PedestrianSource;
import ar.edu.itba.pedestriansim.gui.Camera;

public class AreaFileParser {

	public void load(PedestrianSim simulation, File sourceFile, Camera _camera) {
		try {
			Scanner scanner = new Scanner(sourceFile);
			while (scanner.hasNextLine()) {
				String[] values = scanner.nextLine().split(" ");
				float x, y;
				switch (values[0]) {
					case "rectangle":
						x = Float.parseFloat(values[1]);
						y = Float.parseFloat(values[2]);
						float width = Float.parseFloat(values[3]);
						float height = Float.parseFloat(values[4]);
						simulation.getScene().addObstacle(new Rectangle(x, y, width, height));
						break;
					case "source":
						PedestrianMovementStrategy strategy = new PedestrianMovementStrategy();
						x = Float.parseFloat(values[1]);
						y = Float.parseFloat(values[2]);
						strategy.append(new Vector2f(Float.parseFloat(values[3]), Float.parseFloat(values[4])));
						if (values.length > 5) {
							strategy.append(new Vector2f(Float.parseFloat(values[5]), Float.parseFloat(values[6])));
						}
						PedestrianSource source = new PedestrianSource(new Vector2f(x, y), strategy, simulation.getScene());
						simulation.getScene().addSource(source);
						break;
					case "zoom":
						_camera.setZoom(Float.parseFloat(values[1]));
						break;
					case "location":
						_camera.scrollX(Float.parseFloat(values[1]));
						_camera.scrollY(Float.parseFloat(values[2]));
					default:
						break;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
}
