package ar.edu.itba.pedestriansim.metric;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;
import ar.edu.itba.pedestriansim.back.entity.physics.Collitions;
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

	private Supplier<DymaimcFileStep> _stepsSupplier;
	private Map<Integer, StaticFileLine> allPedestrianStaticInfoById = Maps.newHashMap();
	private List<CollitionMetric> collitionMetrics;
	private List<SimpleMetric> simpleMetrics;
	private List<Metric> allMetrics;
	private FileWriter outputFileWriter;
	private Boolean prettyPrint;

	public CalculateMetricsFromFile(Supplier<StaticFileLine> staticStepSupplier, Supplier<DymaimcFileStep> stepsSupplier, FileWriter outputFileWriter,
			Boolean prettyPrint) {
		_stepsSupplier = stepsSupplier;
		this.prettyPrint = prettyPrint;
		boolean staticSupplierFinished;
		do {
			StaticFileLine fileLine = staticStepSupplier.get();
			staticSupplierFinished = fileLine == null;
			if (!staticSupplierFinished) {
				allPedestrianStaticInfoById.put(fileLine.id(), fileLine);
			}
		} while (!staticSupplierFinished);
		this.outputFileWriter = outputFileWriter;
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
		List<PedestrianDynamicLineInfo> pedestrians = step.pedestriansInfo();
		for (int i = 0; i < pedestrians.size(); i++) {
			PedestrianDynamicLineInfo p1 = pedestrians.get(i);
			for (int j = i + 1; j < pedestrians.size(); j++) {
				PedestrianDynamicLineInfo p2 = pedestrians.get(j);
				// TODO: esto es una simple verificacion de distancia, podria
				// ser mucho mas eficiente!
				float radius1 = allPedestrianStaticInfoById.get(p1.id()).radius();
				Shape cs1 = new Circle(p1.center().getX(), p1.center().getY(), radius1);
				float radius2 = allPedestrianStaticInfoById.get(p2.id()).radius();
				Shape cs2 = new Circle(p2.center().getX(), p2.center().getY(), radius2);
				if (Collitions.touching(cs1, cs2)) {
					for (CollitionMetric metric : collitionMetrics) {
						metric.onCollition(delta, p1.id(), p2.id());
					}
				}
			}
			for (SimpleMetric metric : simpleMetrics) {
				metric.update(delta, p1, allPedestrianStaticInfoById.get(p1.id()));
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
				metric.appendResults(outputFileWriter, prettyPrint);
			}
			outputFileWriter.append("\n");
			outputFileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
