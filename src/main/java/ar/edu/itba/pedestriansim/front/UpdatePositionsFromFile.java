package ar.edu.itba.pedestriansim.front;

import java.util.Map;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;
import ar.edu.itba.pedestriansim.back.logic.PedestrianAreaStep;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

public class UpdatePositionsFromFile extends PedestrianAreaStep {
	
	private final int framesToSkip = 2;
	private int frameIndex = 0;

	private Supplier<DymaimcFileStep> _stepsSupplier;
	private Map<Integer, StaticFileLine> staticInfoById = Maps.newHashMap();
	
	public UpdatePositionsFromFile(Supplier<StaticFileLine> staticStepSupplier, Supplier<DymaimcFileStep> stepsSupplier) {
		_stepsSupplier = stepsSupplier;
		boolean staticSupplierFinished;
		do {
			StaticFileLine fileLine = staticStepSupplier.get();	
			staticSupplierFinished = fileLine == null;
			if (!staticSupplierFinished) {
				staticInfoById.put(fileLine.id(), fileLine);
			}
		} while(!staticSupplierFinished);
	}

	@Override
	public void update(PedestrianArea input) {
		DymaimcFileStep step = _stepsSupplier.get();
		if (step == null) {
			return;	// XXX: simulation finished!
		}
		frameIndex++;
		if (frameIndex <= framesToSkip) {
			return;
		}
		frameIndex = 0;
		input.pedestrians().clear();
		for (PedestrianDynamicLineInfo dynamicInfo : step.pedestriansInfo()) {
			StaticFileLine staticInfo = staticInfoById.get(dynamicInfo.id());
			Pedestrian p = new Pedestrian(staticInfo.id(), staticInfo.team(), new RigidBody(staticInfo.mass(), dynamicInfo.center(), staticInfo.radius()));
			p.pedestrianRepulsionForceValues().setAlpha(staticInfo.pedestrianAlpha()).setBeta(staticInfo.pedestrianBeta());
			input.addPedestrian(p);
			p.getFuture().getBody().setCenter(dynamicInfo.futureCenter());
		}
	}

}
