package ar.edu.itba.pedestriansim.back.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class PedestrianAreaStateFileWriter extends Component {

	private final float _stepInterval;
	private float _timeSinceLastWrite;
	private PedestrianAreaFileSerializer _serializer;
	
	private PedestrianArea _pedestrianArea;

	private BufferedWriter _dynamicFileWriter;
	private BufferedWriter _staticFileWriter;
	private Set<Pedestrian> allPedestrianHistory = Sets.newHashSet();

	public PedestrianAreaStateFileWriter(PedestrianArea pedestrianArea, File directory, float stepInterval) {
		Preconditions.checkArgument(stepInterval >= 0);
		_stepInterval = stepInterval;
		_timeSinceLastWrite = 0;
		_serializer = new PedestrianAreaFileSerializer(pedestrianArea, directory);
		_pedestrianArea = pedestrianArea;
	}

	@Override
	public void onStart() {
		super.onStart();
		try {
			_staticFileWriter = _serializer.createStaticFile();
			_serializer.saveSimulationTime(_staticFileWriter, _pedestrianArea.delta());
			_dynamicFileWriter = _serializer.createDynamicWriter();
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
		try {
			for (Pedestrian pedestrian : _pedestrianArea.getPedestrians()) {
				boolean added = allPedestrianHistory.add(pedestrian);
				if (added) {
					_serializer.saveStaticStep(_staticFileWriter, pedestrian);
				}
			}
			_serializer.saveStep(_dynamicFileWriter);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	@Override
	public void onEnd() {
		try {
			_staticFileWriter.close();
			_dynamicFileWriter.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
