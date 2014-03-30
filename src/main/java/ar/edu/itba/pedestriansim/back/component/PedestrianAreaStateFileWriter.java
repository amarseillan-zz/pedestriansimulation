package ar.edu.itba.pedestriansim.back.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

import com.google.common.base.Preconditions;

public class PedestrianAreaStateFileWriter extends Component {

	private final static String LINE_BREAK = "\n";
	private final static String SEPARATOR = " ";
	private final static String COMMA = ",";
	
	private PedestrianArea _pedestrianArea;
	private BufferedWriter _outputWriter;
	private final float _stepInterval;
	private float _timeSinceLastWrite;
	private BigDecimal _step = new BigDecimal(0).setScale(2);

	public PedestrianAreaStateFileWriter(PedestrianArea pedestrianArea, File outputFile, float stepInterval) {
		_pedestrianArea = pedestrianArea;
		Preconditions.checkArgument(stepInterval >= 0);
		_stepInterval = stepInterval;
		_timeSinceLastWrite = 0;
		try {
			_outputWriter = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		_timeSinceLastWrite += elapsedTimeInSeconds;
		if (_timeSinceLastWrite < _stepInterval) {
			return; // Not ready to write to file yet...
		}
		_timeSinceLastWrite = 0;
		_step = _step.add(new BigDecimal(elapsedTimeInSeconds)).setScale(2, RoundingMode.HALF_DOWN);
		try {
			_outputWriter.write(_step.toString() + LINE_BREAK);
			logPedestrianArea();
			_outputWriter.write(LINE_BREAK);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	private void logPedestrianArea() throws IOException {
		StringBuilder line = new StringBuilder();
		for (Pedestrian pedestrian : _pedestrianArea.getPedestrians()) {
			line.append(pedestrian.getId().toString());
			line.append(SEPARATOR);
			Vector2f center = pedestrian.getBody().getCenter();
			line.append(String.format("%.2f", center.x));
			line.append(COMMA);
			line.append(String.format("%.2f", center.y));
			line.append(SEPARATOR);
		}
		_outputWriter.write(line.toString());
	}

	@Override
	public void onEnd() {
		try {
			_outputWriter.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
