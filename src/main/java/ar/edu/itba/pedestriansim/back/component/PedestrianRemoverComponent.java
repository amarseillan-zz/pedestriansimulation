package ar.edu.itba.pedestriansim.back.component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.event.Event;
import ar.edu.itba.pedestriansim.back.event.EventDispatcher;
import ar.edu.itba.pedestriansim.back.event.EventListener;

import com.google.common.collect.Sets;

public class PedestrianRemoverComponent implements Updateable, EventListener {

	private static final EventDispatcher dispatcher = EventDispatcher.instance();
	
	private Set<Pedestrian> pendingForRemoval = Sets.newHashSet();
	private final PedestrianArea _scene;
	
	public PedestrianRemoverComponent(PedestrianArea scene) {
		_scene = scene;
	}

	@Override
	public void update(float elapsedTimeInSeconds) {
		List<Pedestrian> toRemove = new LinkedList<>();
		for (Pedestrian subject : _scene.getPedestrians()) {
			if (!pendingForRemoval.contains(subject) && subject.isOnFinalTarget()) {
				toRemove.add(subject);
			}
		}
		if (!toRemove.isEmpty()) {
			dispatcher.dispatch(new RemovePedestrianEvent(this, toRemove), 1);
		}
	}
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof RemovePedestrianEvent) {
			Collection<Pedestrian> toRemove = ((RemovePedestrianEvent) event)._toRemove;
			pendingForRemoval.removeAll(toRemove);
			_scene.removePedestrians(toRemove);
		}
	}

	private static class RemovePedestrianEvent extends Event {

		Collection<Pedestrian> _toRemove;

		public RemovePedestrianEvent(EventListener sender, Collection<Pedestrian> toRemove) {
			super(sender, sender);
			_toRemove = toRemove;
		}
		
	}
	
}
