package ar.edu.itba.pedestriansim.metric;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;
import ar.edu.itba.pedestriansim.metric.component.VelocityByDensity;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

public class VBDFromFile {

	private Supplier<DymaimcFileStep> _stepsSupplier;
	private Map<Integer, StaticFileLine> allPedestrianStaticInfoById = Maps.newHashMap();
	private VelocityByDensity vbd;

	public VBDFromFile(Supplier<StaticFileLine> staticStepSupplier, Supplier<DymaimcFileStep> stepsSupplier) {
		_stepsSupplier = stepsSupplier;
		boolean staticSupplierFinished;
		do {
			StaticFileLine fileLine = staticStepSupplier.get();
			staticSupplierFinished = fileLine == null;
			if (!staticSupplierFinished) {
				allPedestrianStaticInfoById.put(fileLine.id(), fileLine);
			}
		} while (!staticSupplierFinished);

		vbd = new VelocityByDensity();
	}

	public List<Pair<Long, Float>> runMetrics(float delta) {
		while (update(delta));
		return this.vbd.getVBDs();
	}

	public boolean update(float delta) {
		
		vbd.onIterationStart();
		
		DymaimcFileStep step = _stepsSupplier.get();
		if (step == null) {
			return false; // XXX: simulation finished!
		}
		List<PedestrianDynamicLineInfo> pedestrians = step.pedestriansInfo();
		for (int i = 0; i < pedestrians.size(); i++) {
			PedestrianDynamicLineInfo p1 = pedestrians.get(i);
			vbd.update(delta, p1, allPedestrianStaticInfoById.get(p1.id()));
		}
		vbd.onIterationEnd();
		return true;
	}

}
