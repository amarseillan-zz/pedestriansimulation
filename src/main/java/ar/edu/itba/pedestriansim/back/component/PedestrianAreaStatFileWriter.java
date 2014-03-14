package ar.edu.itba.pedestriansim.back.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;

import com.google.common.base.Preconditions;

public class PedestrianAreaStatFileWriter extends Component {

	private final static String LINE_BREAK = "\n";
	private final static String SPEARATOR = ",";
	
	private PedestrianArea _pedestrianArea;
	private BufferedWriter _outputWriter;
	private final float _stepInterval;
	private float _timeSiceLastWrite;
	private BigDecimal _step = new BigDecimal(0).setScale(2);

	public PedestrianAreaStatFileWriter(PedestrianArea pedestrianArea, File outputFile, float stepInterval) {
		_pedestrianArea = pedestrianArea;
		Preconditions.checkArgument(stepInterval >= 0);
		_stepInterval = stepInterval;
		_timeSiceLastWrite = 0;
		try {
			_outputWriter = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		_timeSiceLastWrite += elapsedTimeInSeconds;
		if (_timeSiceLastWrite < _stepInterval) {
			return; // Not ready to write to file yet...
		}
		_timeSiceLastWrite = 0;
		_step = _step.add(new BigDecimal(elapsedTimeInSeconds)).setScale(2, RoundingMode.HALF_DOWN);
		try {
			_outputWriter.write(_step.toString() + LINE_BREAK);
			logPedestrianArea();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	private void logPedestrianArea() throws IOException {
		for (Pedestrian pedestrian : _pedestrianArea.getPedestrians()) {
			StringBuilder line = new StringBuilder();
			line.append(pedestrian.getId().toString());
			Vector2f center = pedestrian.getBody().getCenter();
			line.append(SPEARATOR + center.x);
			line.append(SPEARATOR + center.y);
			line.append(LINE_BREAK);
			_outputWriter.write(line.toString());
		}
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
