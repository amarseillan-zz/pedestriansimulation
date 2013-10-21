package ar.edu.itba.pedestriansim.gui;

public abstract class InputKey {

	private boolean _oncePerClick;
	private boolean _pressed;

	public InputKey() {
		this(false);
	}

	public InputKey(boolean oncePerTap) {
		_oncePerClick = oncePerTap;
		_pressed = false;
	}

	public void onKeyPressed() {
		if (_pressed && _oncePerClick) {
			return;
		}
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
