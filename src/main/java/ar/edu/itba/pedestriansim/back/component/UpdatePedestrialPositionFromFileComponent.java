package ar.edu.itba.pedestriansim.back.component;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;

import java.util.Collection;
import java.util.Map;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.DymaimcFileStep;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;
import ar.edu.itba.pedestriansim.back.entity.Pedestrians;
import ar.edu.itba.pedestriansim.back.physics.RigidBody;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class UpdatePedestrialPositionFromFileComponent extends Component {

	private static final Function<PedestrianDynamicLineInfo, Integer> getId = new GetId();

	private PedestrianArea _pedestrianArea;
	private Supplier<DymaimcFileStep> _stepsSupplier;
	private Map<Integer, StaticFileLine> allPedestrianStaticInfoById = Maps.newHashMap();
	
	public UpdatePedestrialPositionFromFileComponent(PedestrianArea pedestrianArea, Supplier<StaticFileLine> staticStepSupplier, Supplier<DymaimcFileStep> stepsSupplier) {
		_pedestrianArea = pedestrianArea;
		_stepsSupplier = stepsSupplier;
		boolean staticSupplierFinished;
		do {
			StaticFileLine fileLine = staticStepSupplier.get();	
			staticSupplierFinished = fileLine == null;
			if (!staticSupplierFinished) {
				allPedestrianStaticInfoById.put(fileLine.id(), fileLine);
			}
		} while(!staticSupplierFinished);
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		DymaimcFileStep step = _stepsSupplier.get();
		if (step == null) {
			return;	// XXX: simulation finished!
		}
		Collection<PedestrianDynamicLineInfo> pedestriansInfo = step.pedestrialsInfo();
		Map<Integer, PedestrianDynamicLineInfo> pedestrianInfoById = asMap(pedestriansInfo);
		Collection<Integer> newPedestrianInfoIds = Collections2.transform(pedestriansInfo, getId);
		Collection<Integer> existingPedestriansIds = Collections2.transform(_pedestrianArea.getPedestrians(), Pedestrians.getId());
		// Kill dead pedestrians
		Collection<Integer> deadPedestriansIds = deadPedestrians(existingPedestriansIds, newPedestrianInfoIds); 
		_pedestrianArea.removePedestrians(new InIdList(deadPedestriansIds));
		// Update existing ones
		for (Pedestrian pedestrian : _pedestrianArea.getPedestrians()) {
			PedestrianDynamicLineInfo dynamicInfo = pedestrianInfoById.get(pedestrian.getId());
			pedestrian.getBody().getCenter().set(dynamicInfo.center());
			pedestrian.getFuture().getBody().getCenter().set(dynamicInfo.futureCenter());
		}
		// Make new pedestrians appear
		Collection<Integer> bornPedestriansIds = bornPedestrians(existingPedestriansIds, newPedestrianInfoIds);
		for (Integer id : bornPedestriansIds) {
			PedestrianDynamicLineInfo info = pedestrianInfoById.get(id);
			StaticFileLine staticInfo = allPedestrianStaticInfoById.get(id);
			Pedestrian born = new Pedestrian(id, staticInfo.team(), new RigidBody(staticInfo.mass(), info.center(), staticInfo.radius()));
			_pedestrianArea.addPedestrian(born);
		}
	}

	private Map<Integer, PedestrianDynamicLineInfo> asMap(Collection<PedestrianDynamicLineInfo> pedestriansInfo) {
		Map<Integer, PedestrianDynamicLineInfo> pedestrianInfoById = Maps.newHashMap();
		for (PedestrianDynamicLineInfo pedestrianInfo : pedestriansInfo) {
			pedestrianInfoById.put(pedestrianInfo.id(), pedestrianInfo);
		}
		return pedestrianInfoById;
	}
	
	private Collection<Integer> deadPedestrians(Collection<Integer> existingPedestriansIds, Collection<Integer> newPedestrianInfoIds) {
		return filter(existingPedestriansIds, not(in(newPedestrianInfoIds)));
	}

	private Collection<Integer> bornPedestrians(Collection<Integer> existingPedestriansIds, Collection<Integer> newPedestrianInfoIds) {
		return filter(newPedestrianInfoIds, not(in(existingPedestriansIds)));
	}

	private static class GetId implements Function<PedestrianDynamicLineInfo, Integer> {
		@Override
		public Integer apply(PedestrianDynamicLineInfo input) {
			return input.id();
		}
	}

	private static class InIdList implements Predicate<Pedestrian> {

		private Collection<Integer> _ids;
		
		public InIdList(Collection<Integer> ids) {
			_ids = ids;
		}
		
		@Override
		public boolean apply(Pedestrian input) {
			return _ids.contains(input.getId());
		}
		
	}
}
