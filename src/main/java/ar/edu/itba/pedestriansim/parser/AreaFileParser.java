package ar.edu.itba.pedestriansim.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import ar.edu.itba.pedestriansim.back.PedestrianSim;
import ar.edu.itba.pedestriansim.gui.Camera;

import com.google.common.base.Strings;

public class AreaFileParser {

	public PedestrianSim load(String dir, Camera camera, Properties setup) throws IOException {
		int withGrids = Integer.valueOf(setup.getProperty("width.grids"));
		int heightGrids = Integer.valueOf(setup.getProperty("height.grids"));
		int gridSize = Integer.valueOf(setup.getProperty("grid.size"));
		PedestrianSim simulation = new PedestrianSim(withGrids, heightGrids, gridSize);
		loadSources(dir, setup, simulation);
		String objectsFileame = setup.getProperty("objects");
		if (!Strings.isNullOrEmpty(objectsFileame)) {
			parseObjectsFile(new File(dir + "/" + objectsFileame), simulation, camera);
		}
		String zoomString = setup.getProperty("zoom");
		if (!Strings.isNullOrEmpty(zoomString)) {
			camera.setZoom(Float.parseFloat(zoomString));
		}
		String[] location = Strings.nullToEmpty(setup.getProperty("location")).split(",");
		if (location.length == 2) {
			camera.scrollX(Float.parseFloat(location[0]));
			camera.scrollY(Float.parseFloat(location[1]));
		}
		return simulation;
	}
	
	private void loadSources(String directory, Properties setup, PedestrianSim simulation) throws IOException {
		String[] sourceConfig = Strings.nullToEmpty(setup.getProperty("source")).split(", ");
		PedestrianSourceParser sourceParser = new PedestrianSourceParser();
		for (String sourcFile : sourceConfig) {
			File sourceFile = new File(directory + File.separator + sourcFile);
			simulation.getScene().addSource(sourceParser.load(sourceFile, simulation.getScene()));
		}
	}
	
	private void parseObjectsFile(File file, PedestrianSim simulation, Camera camera) {
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
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
			case "zoom":
				
				break;
			case "location":
				
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
	}
}
