package ar.edu.itba.pedestriansim.front;

public abstract class InputKey {

	private boolean _pressed;

	public InputKey() {
		_pressed = false;
	}

	public void onKeyPressed() {
		_pressed = true;
		pressed();
	}

	public abstract void pressed();

	public void onKeyReleased() {
		_pressed = false;
	}

	public boolean isPressed() {
		return _pressed;
	}

	public void setPressed() {
		_pressed = true;
	}
}
