package ar.edu.itba.pedestriansim.back.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianSim;

public class PedestrianFileParser {
	
	public void laod(PedestrianSim pedestrianSim, File file) throws IOException {
		Properties propertyFile = new Properties();
		propertyFile.load(new FileInputStream(file));
		for (Object value : propertyFile.values()) {
			String[] parts = value.toString().split(",");
			float mass = Float.parseFloat(parts[0].trim());
			Vector2f location = new Vector2f(ParserUtil.parseArray(parts[2].trim()));
			Vector2f target = new Vector2f(ParserUtil.parseArray(parts[3].trim()));
			Pedestrian pedestrian = new Pedestrian(mass, location, target);
			pedestrian.setMaxVelocity(Float.parseFloat(parts[1].trim()));
			pedestrianSim.addPedestrian(pedestrian);
		}
	}
}
