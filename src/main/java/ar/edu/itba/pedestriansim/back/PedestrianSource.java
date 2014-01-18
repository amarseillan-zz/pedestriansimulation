package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.event.Event;
import ar.edu.itba.common.event.EventDispatcher;
import ar.edu.itba.common.event.EventListener;
import ar.edu.itba.common.rand.GaussianRandomGenerator;
import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.factory.PedestrianFactory;

public class PedestrianSource implements EventListener {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();
	
	private PedestrianFactory _pedestrianFactory;
	private RandomGenerator _produceDelayGenerator = new GaussianRandomGenerator(3, 2);
	private RandomGenerator _pedestrianAmountGenerator = new UniformRandomGenerator(5, 9);
	private final RandomGenerator _initialLocationGenerator;
	private float _radius;
	private Vector2f _location;
	private PedestrianArea _pedestrianArea;
	private PedestrianTargetList _targetList;
	private int _team;
	private int _totalProduced, _produceLimit;

	public PedestrianSource(PedestrianFactory pedestrianFactory, Vector2f location, float radius, PedestrianTargetList targetList, PedestrianArea pedestrianArea, int team) {
		_pedestrianFactory = pedestrianFactory;
		_radius = radius;
		_location = location;
		_pedestrianArea = pedestrianArea;
		_targetList = targetList;
		_team = team;
		_initialLocationGenerator = new UniformRandomGenerator(-getRadius(), getRadius());
		_totalProduced = 0;
		_produceLimit = -1;
	}

	public void setProduceLimit(int produceLimit) {
		_produceLimit = produceLimit;
	}

	public void setProduceDelayGenerator(RandomGenerator produceDelayGenerator) {
		_produceDelayGenerator = produceDelayGenerator;
	}

	public void setPedestrianAmountGenerator(RandomGenerator pedestrianAmountGenerator) {
		_pedestrianAmountGenerator = pedestrianAmountGenerator;
	}

	public void start() {
		schedule();
	}

	public Vector2f getLocation() {
		return _location;
	}

	private void schedule() {
		if (_produceLimit < 0 || _totalProduced < _produceLimit) {
			float delay = _produceDelayGenerator.generate();
			dispatcher.dispatch(new ProducePedestrianEvent(this), delay);
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof ProducePedestrianEvent) {
			produce();
			schedule();
		}
	}

	public void produce() {
		int amount = (int) _pedestrianAmountGenerator.generate();
		if (_produceLimit > 0) {
			amount = Math.min(amount, _produceLimit - _totalProduced);
		}
		for (int i = 0; i < amount; i++) {
			// FIXME: aqui se podria simplemente crear el Body de un peaton y ver si etsa libre,
			// SI el lugar esta libre, ahi recien crear el peaton
			_totalProduced++;
			Pedestrian pedestrian = _pedestrianFactory.build(new Vector2f(), _team, _targetList);
			boolean isEmpty;
			do {
				float x = _location.x + _initialLocationGenerator.generate();
				float y = _location.y + _initialLocationGenerator.generate();
				Vector2f pedestrianLocation = pedestrian.getBody().getCenter();
				pedestrian.translate(x - pedestrianLocation.x, y - pedestrianLocation.y);
				isEmpty = _pedestrianArea.hasCollitions(pedestrian);
			} while (!isEmpty);
			_pedestrianArea.addPedestrian(pedestrian);
		}
	}
	
	public float getRadius() {
		return _radius;
	}

	private static final class ProducePedestrianEvent extends Event {

		public ProducePedestrianEvent(PedestrianSource sender) {
			super(sender, sender);
		}

	}
}
