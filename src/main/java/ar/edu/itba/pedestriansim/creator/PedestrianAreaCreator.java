package ar.edu.itba.pedestriansim.creator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianFactory;
import ar.edu.itba.pedestriansim.gui.Camera;

import com.google.common.base.Optional;

@Component
public class PedestrianAreaCreator {

	@Autowired
	private PedestrianAppConfig config;

	@Autowired
	private PedestrianFactory pedestrianfactory;
	
	public PedestrianArea produce(Camera camera) {
		try {
			PedestrianArea pedestrianArea = config.getPedestrianArea();
			loadSources(pedestrianArea);
			loadObstacles(pedestrianArea);
			setupView(camera);
			return pedestrianArea;
		} catch (IOException e) {
    		throw new IllegalStateException(e);
    	}
	}
	
	private void loadSources(PedestrianArea pedestrianArea) throws IOException {
		PedestrianSourceCreator	sourceCreator = new PedestrianSourceCreator(pedestrianArea);
		for (Properties sourceConfig : config.getPedestrianSources()) {
			pedestrianArea.addSource(sourceCreator.produce(sourceConfig, pedestrianfactory));
		}
	}
	
	private void loadObstacles(PedestrianArea pedestrianArea) {
		InputStream is = config.getObjectFile();
		if (is == null) {
			return;
		}
		Scanner scanner = new Scanner(is);
		while (scanner.hasNextLine()) {
			String[] values = scanner.nextLine().split(" ");
			float x, y;
			switch (values[0]) {
			case "rectangle":
				x = Float.parseFloat(values[1]);
				y = Float.parseFloat(values[2]);
				float width = Float.parseFloat(values[3]);
				float height = Float.parseFloat(values[4]);
				pedestrianArea.addObstacle(new Rectangle(x, y, width, height));
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
				pedestrianArea.addObstacle(new Line(x1, y1, x2, y2));
			default:
				break;
			}
		}
		scanner.close();
	}

	private void setupView(Camera camera) {
		Optional<Integer> zoom = config.getOptional("zoom", Integer.class);
		if (zoom.isPresent()) {
			camera.setZoom(zoom.get());
		}
		Optional<String[]> location = config.getLocation();
		if (location.isPresent()) {
			camera.scrollX(Float.parseFloat(location.get()[0]));
			camera.scrollY(Float.parseFloat(location.get()[1]));
		}
	}
}
