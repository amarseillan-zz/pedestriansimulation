package ar.edu.itba.pedestriansim.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.back.PedestrianSource;
import ar.edu.itba.pedestriansim.gui.Camera;

public class AreaFileParser {

	public void load(PedestrianSim simulation, File file, Camera _camera) throws IOException {
		try {
			Scanner scanner = new Scanner(file);
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
						File sourceFile = new File(file.getParentFile().getAbsolutePath() + File.separator + values[1]);
						PedestrianSource source = new PedestrianSourceParser().load(sourceFile, simulation.getScene());
						simulation.getScene().addSource(source);
						break;
					case "zoom":
						_camera.setZoom(Float.parseFloat(values[1]));
						break;
					case "location":
						_camera.scrollX(Float.parseFloat(values[1]));
						_camera.scrollY(Float.parseFloat(values[2]));
						break;
					case "line":
						float x1 = Float.parseFloat(values[1]);
						float y1 = Float.parseFloat(values[2]);
						float x2 = Float.parseFloat(values[3]);
						float y2 = Float.parseFloat(values[4]);
						simulation.getScene().addObstacle(new Line(x1, y1, x2, y2));
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
