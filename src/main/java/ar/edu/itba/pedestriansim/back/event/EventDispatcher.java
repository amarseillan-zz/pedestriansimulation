package ar.edu.itba.pedestriansim.back.event;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import ar.edu.itba.pedestriansim.back.Updateable;

public class EventDispatcher implements Updateable {

	private final static EventDispatcher _instance = new EventDispatcher();

	public static EventDispatcher instance() {
		return _instance;
	}

	private PriorityQueue<PendingEvent> pendingEvents = new PriorityQueue<>();
	private List<PendingEvent> newEvents = new LinkedList<>();
	
	public void dispatchNow(Event event) {
		dispatch(event, 0);
	}

	public void dispatch(Event event, float delay) {
		if (delay <= 0) {
			event.send();
		} else {
			newEvents.add(new PendingEvent(delay, event));
		}
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		pendingEvents.addAll(newEvents);
		newEvents.clear();
		Iterator<PendingEvent> itarator = pendingEvents.iterator();
		while (itarator.hasNext()) {
			PendingEvent pendingEvent = itarator.next();
			pendingEvent._delay -= elapsedTimeInSeconds;
			if (pendingEvent._event instanceof Updateable) {
				Updateable event = (Updateable) pendingEvent._event;
				event.update(elapsedTimeInSeconds);
			}
			if (pendingEvent._delay <= 0) {
				itarator.remove();
				pendingEvent._event.send();
			}
		}
	}

	private static class PendingEvent implements Comparable<PendingEvent> {

		Float _delay;
		Event _event;

		public PendingEvent(Float delay, Event event) {
			_delay = delay;
			_event = event;
		}

		@Override
		public int compareTo(PendingEvent o) {
			return _delay.compareTo(o._delay);
		}

	}
}
