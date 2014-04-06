package ar.edu.itba.pedestriansim.metric;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;
import ar.edu.itba.pedestriansim.back.physics.Collitions;
import ar.edu.itba.pedestriansim.metric.component.CollitionCount;
import ar.edu.itba.pedestriansim.metric.component.CollitionCountPerInstant;
import ar.edu.itba.pedestriansim.metric.component.CollitionMetric;
import ar.edu.itba.pedestriansim.metric.component.Metric;
import ar.edu.itba.pedestriansim.metric.component.SimpleMetric;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

public class CalculateMetricsFromFile {


	private Supplier<DymaimcFileStep> _stepsSupplier;
	private Map<Integer, StaticFileLine> allPedestrianStaticInfoById = Maps.newHashMap();
	private List<CollitionMetric> collitionMetrics;
	private List<SimpleMetric> simpleMetrics;
	private List<Metric> allMetrics;
	private String outputFile;
	
	public CalculateMetricsFromFile(Supplier<StaticFileLine> staticStepSupplier, Supplier<DymaimcFileStep> stepsSupplier, String outputFile) {
		_stepsSupplier = stepsSupplier;
		boolean staticSupplierFinished;
		do {
			StaticFileLine fileLine = staticStepSupplier.get();	
			staticSupplierFinished = fileLine == null;
			if (!staticSupplierFinished) {
				allPedestrianStaticInfoById.put(fileLine.id(), fileLine);
			}
		} while(!staticSupplierFinished);
		
		this.outputFile = outputFile;
		collitionMetrics = new ArrayList<CollitionMetric>(5);
		simpleMetrics = new ArrayList<SimpleMetric>(5);
		allMetrics = new ArrayList<Metric>(10);
		CollitionCount collitionCount = new CollitionCount();
		CollitionCountPerInstant collitionCountPerInstant = new CollitionCountPerInstant();
		collitionMetrics.add(collitionCount);
		allMetrics.add(collitionCount);
		collitionMetrics.add(collitionCountPerInstant);
		allMetrics.add(collitionCountPerInstant);
	}

	public boolean update(float delta) {
		for (CollitionMetric metric: collitionMetrics) {
			metric.onIterationStart();
		}
		for (SimpleMetric metric: simpleMetrics) {
			metric.onIterationStart();
		}
		DymaimcFileStep step = _stepsSupplier.get();
		if (step == null) {
			return false;	// XXX: simulation finished!
		}
		List<PedestrianDynamicLineInfo> pedestrians = step.pedestrialsInfo();
		for (int i = 0; i<pedestrians.size(); i++) {
			PedestrianDynamicLineInfo p1 = pedestrians.get(i);
			for (int j = i+1; j<pedestrians.size(); j++) {
				PedestrianDynamicLineInfo p2 = pedestrians.get(j);
				float radius1 = allPedestrianStaticInfoById.get(p1.id()).radius();
				Shape cs1 = new Circle(p1.center().getX(), p1.center().getY(), radius1);
				float radius2 = allPedestrianStaticInfoById.get(p2.id()).radius();
				Shape cs2 = new Circle(p2.center().getX(), p2.center().getY(), radius2);
				if (Collitions.touching(cs1, cs2)) {
					for (CollitionMetric metric: collitionMetrics) {
					metric.onCollition(delta, p1.id(), p2.id());
					}
				}
			}
			for (SimpleMetric metric: simpleMetrics) {
				metric.update(p1, allPedestrianStaticInfoById.get(p1.id()));
			}
		}
		for (CollitionMetric metric: collitionMetrics) {
			metric.onIterationEnd();
		}
		for (SimpleMetric metric: simpleMetrics) {
			metric.onIterationEnd();
		}
		
		return true;
	}
	
	public void onSimulationEnd() {
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile);
			for (Metric metric: allMetrics) {
				metric.appendResults(writer);
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
