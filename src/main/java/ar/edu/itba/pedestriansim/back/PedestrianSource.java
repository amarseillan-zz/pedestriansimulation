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

	private final RandomGenerator _produceDelayGenerator = new GaussianRandomGenerator(3, 2);
	private final RandomGenerator _pedestrianAmountGenerator = new UniformRandomGenerator(5, 9);
	private final RandomGenerator _initialLocationGenerator;

	private int _radius;
	private PedestrianArea _pedestrianArea;
	private int lastPedestrianId = 0;
	private Vector2f _location;
	private PedestrianMovementStrategy _strategy;

	public PedestrianSource(Vector2f location, PedestrianMovementStrategy strategy, PedestrianArea pedestrianArea) {
		_radius = 3;
		_location = location;
		_pedestrianArea = pedestrianArea;
		_strategy = strategy;
		_initialLocationGenerator = new UniformRandomGenerator(-getRadius(), getRadius());
		schedule();
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
			float x = _location.x + _initialLocationGenerator.generate();
			float y = _location.y + _initialLocationGenerator.generate();
			Pedestrian pedestrian = new Pedestrian(lastPedestrianId++, new Vector2f(x, y), _strategy.copy());
			_pedestrianArea.addPedestrian(pedestrian);
		}
	}
	
	public int getRadius() {
		return _radius;
	}

	private static final class ProducePedestrianEvent extends Event {

		public ProducePedestrianEvent(PedestrianSource sender) {
			super(sender, sender);
		}

	}
}
