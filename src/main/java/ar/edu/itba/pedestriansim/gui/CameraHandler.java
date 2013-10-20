package ar.edu.itba.pedestriansim.gui;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class CameraHandler implements KeyListener {

	private Camera _camera;

	private final Map<Integer, ActionKey> handlers = new HashMap<>();

	public CameraHandler(Camera camera) {
		_camera = camera;
		handlers.put(Input.KEY_EQUALS, new ZoomOut());
		handlers.put(Input.KEY_ADD, new ZoomOut());
		handlers.put(Input.KEY_MINUS, new ZoomIn());
		handlers.put(Input.KEY_SUBTRACT, new ZoomIn());

		handlers.put(Input.KEY_UP, new ScrollUp());
		handlers.put(Input.KEY_DOWN, new ScrollDown());
		handlers.put(Input.KEY_LEFT, new ScrollLeft());
		handlers.put(Input.KEY_RIGHT, new ScrollRight());

		handlers.put(Input.KEY_W, new ScrollUp());
		handlers.put(Input.KEY_S, new ScrollDown());
		handlers.put(Input.KEY_A, new ScrollLeft());
		handlers.put(Input.KEY_D, new ScrollRight());
	}

	@Override
	public void setInput(Input input) {}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void inputStarted() {}

	@Override
	public void inputEnded() {}

	@Override
	public void keyPressed(int key, char c) {
		ActionKey action = handlers.get(key);
		if (action != null) {
			action.pressed();
		}
	}

	@Override
	public void keyReleased(int key, char c) {}

	private final class ZoomOut implements ActionKey {
		@Override
		public void pressed() {
			_camera.zoomOut();
		}
	}

	private final class ZoomIn implements ActionKey {
		@Override
		public void pressed() {
			_camera.zoomIn();
		}
	}

	private final class ScrollLeft implements ActionKey {
		@Override
		public void pressed() {
			_camera.scrollLeft();
		}
	}

	private final class ScrollRight implements ActionKey {
		@Override
		public void pressed() {
			_camera.scrollRight();
		}
	}

	private final class ScrollUp implements ActionKey {
		@Override
		public void pressed() {
			_camera.scrollUp();
		}
	}

	private final class ScrollDown implements ActionKey {
		@Override
		public void pressed() {
			_camera.scrollDown();
		}
	}
}
