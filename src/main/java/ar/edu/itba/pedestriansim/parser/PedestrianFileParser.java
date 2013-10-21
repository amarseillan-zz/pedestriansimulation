package ar.edu.itba.pedestriansim.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianMovementStrategy;
import ar.edu.itba.pedestriansim.back.PedestrianSim;

public class PedestrianFileParser {
	
	public void load(PedestrianSim pedestrianSim, File file) {
		try {
			Properties propertyFile = new Properties();
			propertyFile.load(new FileInputStream(file));
			for (Entry<Object, Object> entry : propertyFile.entrySet()) {
				String id = entry.getKey().toString();
				String[] parts = entry.getValue().toString().split(",");
				Vector2f location = new Vector2f(ParserUtil.parseArray(parts[2].trim()));
				PedestrianMovementStrategy strategy = new PedestrianMovementStrategy();
				strategy.append(new Vector2f(ParserUtil.parseArray(parts[3].trim())));
				strategy.append(new Vector2f(ParserUtil.parseArray(parts[4].trim())));
				Pedestrian pedestrian = new Pedestrian(id, location, strategy);
				pedestrian.getBody().setMass(Float.parseFloat(parts[0].trim()));
				pedestrian.setMaxVelocity(Float.parseFloat(parts[1].trim()));
				pedestrianSim.getScene().addPedestrian(pedestrian);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
