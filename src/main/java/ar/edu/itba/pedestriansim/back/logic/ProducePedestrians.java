package ar.edu.itba.pedestriansim.back.logic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSource;
import ar.edu.itba.pedestriansim.back.factory.PedestrianFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ProducePedestrians extends PedestrianAreaStep {

	private final PriorityQueue<ArrivalEvent> _arriveQueue = new PriorityQueue<>();
	private PedestrianFactory _pedestrianFactory;

	public ProducePedestrians(PedestrianFactory pedestrianFactory) {
		_pedestrianFactory = Preconditions.checkNotNull(pedestrianFactory);
	}

	@Override
	public void update(PedestrianArea input) {
		if (_arriveQueue.isEmpty()) {
			queueAll(input.sources());
		}
		List<PedestrianSource> toBeScheduled = Lists.newLinkedList();
		Iterator<ArrivalEvent> it = _arriveQueue.iterator();
		while (it.hasNext()) {
			ArrivalEvent arrival = it.next();
			arrival._delay -= input.timeStep().floatValue();
			if (arrival._delay <= 0) {
				produce(input, arrival._source);
				it.remove();
				toBeScheduled.add(arrival._source);
			}
		}
		queueAll(toBeScheduled);
	}

	private void queueAll(Collection<PedestrianSource> sources) {
		if (!sources.isEmpty()) {
			for (PedestrianSource source : sources) {
				if (source.isEnabled() && (source.produceLimit() < 0 || source.totalProduced() < source.produceLimit())) {
					queue(source);
				}
			}
		}
	}

	private void queue(PedestrianSource source) {
		float delay = source.delay().generate();
		_arriveQueue.add(new ArrivalEvent(delay, source));
	}

	public void produce(PedestrianArea input, PedestrianSource source) {
		int amount = (int) source.amount().generate();
		if (source.produceLimit() > 0) {
			amount = Math.min(amount, source.produceLimit() - source.totalProduced());
		}
		for (int i = 0; i < amount; i++) {
			// FIXME: aqui se podria simplemente crear el Body de un peaton y
			// ver si etsa libre,
			// SI el lugar esta libre, ahi recien crear el peaton
			source.incTotalProduced(1);
			Pedestrian pedestrian = _pedestrianFactory.build(new Vector2f(), source.team(), source.mission());
			int tries = 0;
			do {
				RandomGenerator random = new UniformRandomGenerator(-source.radius(), source.radius());
				float x = source.center().x + random.generate();
				float y = source.center().y + random.generate();
				Vector2f pedestrianLocation = pedestrian.getBody().getCenter();
				pedestrian.translate(x - pedestrianLocation.x, y - pedestrianLocation.y);
			} while (input.hasCollitions(pedestrian) && tries++ < 10);
			pedestrian.setReactionDistance(Pedestrian.DEFAULT_REACTION_DISTANCE);
			input.addPedestrian(pedestrian);
		}
	}

	private static final class ArrivalEvent implements Comparable<ArrivalEvent> {

		private Float _delay;
		private PedestrianSource _source;

		public ArrivalEvent(Float delay, PedestrianSource source) {
			_delay = delay;
			_source = source;
		}

		@Override
		public int compareTo(ArrivalEvent o) {
			return _delay.compareTo(o._delay);
		}
		
		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("delay", _delay).append("team", _source.team()).toString();
		}
	}

}
