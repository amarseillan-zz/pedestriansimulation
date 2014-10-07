package ar.edu.itba.pedestriansim.back.logic;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.RepulsionForceValues;

import com.google.common.collect.Sets;

public class PedestrianAreaStateFileWriter extends PedestrianAreaStep {

	private final static String LINE_BREAK = "\n";
	private final static String SPACE = " ";
	private final static String DASH = "/";

	private final float _stepInterval;
	private float _timeSinceLastWrite;

	private FileWriter _staticFileWriter;
	private FileWriter _dynamicFileWriter;
	private Set<Pedestrian> allPedestrianHistory = Sets.newHashSet();

	public PedestrianAreaStateFileWriter(FileWriter staticFileWriter, FileWriter dynamicFileWriter, float stepInterval) {
		_timeSinceLastWrite = 0;
		_stepInterval = stepInterval;
		_staticFileWriter = staticFileWriter;
		_dynamicFileWriter = dynamicFileWriter;
	}

	@Override
	public void update(PedestrianArea pedestrianArea) {
		_timeSinceLastWrite += pedestrianArea.timeStep().floatValue();
		if (_timeSinceLastWrite < _stepInterval) {
			return; // Not ready to write to file yet...
		}
		_timeSinceLastWrite = 0;
		try {
			for (Pedestrian pedestrian : pedestrianArea.pedestrians()) {
				if (allPedestrianHistory.add(pedestrian)) {
					saveStaticStep(pedestrian);
				}
			}
			saveStep(pedestrianArea);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	private void saveStaticStep(Pedestrian pedestrian) throws IOException {
		_staticFileWriter.append(pedestrian.getId().toString());
		_staticFileWriter.append(DASH);
		_staticFileWriter.append(pedestrian.getTeam() + "");
		_staticFileWriter.append(DASH);
		_staticFileWriter.append(pedestrian.getBody().getMass() + "");
		_staticFileWriter.append(DASH);
		_staticFileWriter.append(pedestrian.getBody().getRadius() + "");
		RepulsionForceValues values = pedestrian.pedestrianRepulsionForceValues();
		_staticFileWriter.append(DASH);
		_staticFileWriter.append(values.alpha() + "");
		_staticFileWriter.append(DASH);
		_staticFileWriter.append(values.beta() + "");
		_staticFileWriter.append(LINE_BREAK);
	}

	private void saveStep(PedestrianArea pedestrianArea) throws IOException {
		BigDecimal step = pedestrianArea.elapsedTime();
		_dynamicFileWriter.write(step + LINE_BREAK);
		StringBuilder line = new StringBuilder();
		for (Pedestrian pedestrian : pedestrianArea.pedestrians()) {
			line.append(pedestrian.getId().toString());
			line.append(DASH);
			Vector2f center = pedestrian.getBody().getCenter();
			line.append(String.format("%.2f", center.x));
			line.append(SPACE);
			line.append(String.format("%.2f", center.y));
			line.append(DASH);
			Vector2f speed = pedestrian.getBody().getVelocity();
			line.append(String.format("%.2f", speed.x));
			line.append(SPACE);
			line.append(String.format("%.2f", speed.y));
			line.append(DASH);
			Vector2f futureCenter = pedestrian.getFuture().getBody().getCenter();
			line.append(String.format("%.2f", futureCenter.x));
			line.append(SPACE);
			line.append(String.format("%.2f", futureCenter.y));
			line.append(LINE_BREAK);
		}
		_dynamicFileWriter.write(line.toString());
	}
}
