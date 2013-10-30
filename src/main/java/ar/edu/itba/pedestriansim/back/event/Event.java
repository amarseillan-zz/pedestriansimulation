package ar.edu.itba.pedestriansim.back.event;


public class Event {

	private EventListener _sender, _receiver;

	public Event(EventListener sender, EventListener receiver) {
		_sender = sender;
		_receiver = receiver;
	}

	public EventListener getSender() {
		return _sender;
	}

	public EventListener getReceiver() {
		return _receiver;
	}
	
	public void send() {
		_receiver.onEvent(this);
	}
}
