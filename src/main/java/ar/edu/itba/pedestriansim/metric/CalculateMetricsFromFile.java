package ar.edu.itba.pedestriansim.metric;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;
import ar.edu.itba.pedestriansim.metric.component.AverageTravelTime;
import ar.edu.itba.pedestriansim.metric.component.AverageVelocity;
import ar.edu.itba.pedestriansim.metric.component.AverageWalkDistance;
import ar.edu.itba.pedestriansim.metric.component.CollitionCount;
import ar.edu.itba.pedestriansim.metric.component.CollitionCountPerInstant;
import ar.edu.itba.pedestriansim.metric.component.CollitionMetric;
import ar.edu.itba.pedestriansim.metric.component.Metric;
import ar.edu.itba.pedestriansim.metric.component.SimpleMetric;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CalculateMetricsFromFile {

	private Map<Integer, StaticFileLine> _staticPedestrianInfoById = Maps.newHashMap();
	private Supplier<DymaimcFileStep> _stepsSupplier;
	private List<CollitionMetric> collitionMetrics;
	private List<SimpleMetric> simpleMetrics;
	private List<Metric> allMetrics;
	private FileWriter _outputFileWriter;
	private Boolean prettyPrint;

	public CalculateMetricsFromFile(Supplier<StaticFileLine> staticStepSupplier, Supplier<DymaimcFileStep> stepsSupplier, FileWriter outputFileWriter, Boolean prettyPrint) {
		_stepsSupplier = stepsSupplier;
		this.prettyPrint = prettyPrint;
		boolean staticSupplierFinished;
		do {
			StaticFileLine fileLine = staticStepSupplier.get();
			staticSupplierFinished = fileLine == null;
			if (!staticSupplierFinished) {
				_staticPedestrianInfoById.put(fileLine.id(), fileLine);
			}
		} while (!staticSupplierFinished);
		_outputFileWriter = outputFileWriter;
		collitionMetrics = Lists.newArrayList();
		simpleMetrics = Lists.newArrayList();
		allMetrics = Lists.newArrayList();
		CollitionCount collitionCount = new CollitionCount();
		CollitionCountPerInstant collitionCountPerInstant = new CollitionCountPerInstant();
		AverageVelocity averageVelocity = new AverageVelocity();
		AverageTravelTime averageTravelTime = new AverageTravelTime();
		AverageWalkDistance averageWalkDistance = new AverageWalkDistance();
		collitionMetrics.add(collitionCount);
		allMetrics.add(collitionCount);
		collitionMetrics.add(collitionCountPerInstant);
		allMetrics.add(collitionCountPerInstant);
		simpleMetrics.add(averageVelocity);
		allMetrics.add(averageVelocity);
		simpleMetrics.add(averageTravelTime);
		allMetrics.add(averageTravelTime);
		simpleMetrics.add(averageWalkDistance);
		allMetrics.add(averageWalkDistance);
		
		VelocityByDensity vbd = new VelocityByDensity();
		simpleMetrics.add(vbd);
		allMetrics.add(vbd);
	}

	public void runMetrics(float delta) {
		while (update(delta))
			;
		onSimulationEnd();
	}

	public boolean update(float delta) {
		for (Metric metric : allMetrics) {
			metric.onIterationStart();
		}
		DymaimcFileStep step = _stepsSupplier.get();
		if (step == null) {
			return false; // XXX: simulation finished!
		}
		List<PedestrianDynamicLineInfo> lines = step.pedestriansInfo();
		for (int i = 0; i < lines.size(); i++) {
			PedestrianDynamicLineInfo linei = lines.get(i);
			for (int j = i + 1; j < lines.size(); j++) {
				PedestrianDynamicLineInfo linej = lines.get(j);
				StaticFileLine staticLinei = _staticPedestrianInfoById.get(linei.id());
				StaticFileLine staticLinej = _staticPedestrianInfoById.get(linej.id());
				float centerDist = linei.center().distance(linej.center());
				if (centerDist <= staticLinei.radius() + staticLinej.radius()) {
					for (CollitionMetric metric : collitionMetrics) {
						metric.onCollition(delta, linei.id(), linej.id());
					}
				}
			}
			for (SimpleMetric metric : simpleMetrics) {
				metric.update(delta, linei, _staticPedestrianInfoById.get(linei.id()));
			}
		}
		for (Metric metric : allMetrics) {
			metric.onIterationEnd();
		}
		return true;
	}

	public void onSimulationEnd() {
		try {
			for (Metric metric : allMetrics) {
				metric.appendResults(_outputFileWriter, prettyPrint);
			}
			_outputFileWriter.append("\n");
			_outputFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
