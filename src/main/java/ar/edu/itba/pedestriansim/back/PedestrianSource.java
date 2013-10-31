package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.event.Event;
import ar.edu.itba.pedestriansim.back.event.EventDispatcher;
import ar.edu.itba.pedestriansim.back.event.EventListener;
import ar.edu.itba.pedestriansim.back.rand.GaussianRandomGenerator;
import ar.edu.itba.pedestriansim.back.rand.RandomGenerator;
import ar.edu.itba.pedestriansim.back.rand.UniformRandomGenerator;

public class PedestrianSource implements EventListener {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();
	private static final PedestrianFactory pedestrianFactory = PedestrianFactory.instance();

	private RandomGenerator _produceDelayGenerator = new GaussianRandomGenerator(3, 2);
	private RandomGenerator _pedestrianAmountGenerator = new UniformRandomGenerator(5, 9);
	private final RandomGenerator _initialLocationGenerator;
	private float _radius;
	private Vector2f _location;
	private PedestrianArea _pedestrianArea;
	private PedestrianTargetList _targetList;
	private int _team;

	public PedestrianSource(Vector2f location, float radius, PedestrianTargetList targetList, PedestrianArea pedestrianArea, int team) {
		_radius = radius;
		_location = location;
		_pedestrianArea = pedestrianArea;
		_targetList = targetList;
		_team = team;
		_initialLocationGenerator = new UniformRandomGenerator(-getRadius(), getRadius());
		schedule();
	}

	public void setProduceDelayGenerator(RandomGenerator produceDelayGenerator) {
		_produceDelayGenerator = produceDelayGenerator;
	}

	public void setPedestrianAmountGenerator(RandomGenerator pedestrianAmountGenerator) {
		_pedestrianAmountGenerator = pedestrianAmountGenerator;
	}

	public Vector2f getLocation() {
		return _location;
	}

	private void schedule() {
		float delay = _produceDelayGenerator.generate();
		dispatcher.dispatch(new ProducePedestrianEvent(this), delay);		
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
		for (int i = 0; i < amount; i++) {
			Pedestrian pedestrian = pedestrianFactory.build(new Vector2f(), _team, _targetList);
			boolean isEmpty;
			do {
				float x = _location.x + _initialLocationGenerator.generate();
				float y = _location.y + _initialLocationGenerator.generate();
				pedestrian.getBody().setLocation(new Vector2f(x, y));
				isEmpty = _pedestrianArea.collides(pedestrian.getBody().getCollitionShape().getShape());
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
