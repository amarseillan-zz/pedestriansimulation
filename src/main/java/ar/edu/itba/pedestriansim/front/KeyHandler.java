package ar.edu.itba.pedestriansim.front;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

import com.google.common.collect.Lists;

public class KeyHandler implements KeyListener {

	private PedestrianAreaRenderer _renderer;
	private Camera _camera;
	private GameContainer _gc;

	private final List<KeyMapping> handlers = Lists.newLinkedList();
	
	public KeyHandler(Camera camera, PedestrianAreaRenderer renderer, GameContainer gc) {
		_camera = camera;
		_renderer = renderer;
		_gc = gc;
		initialize();
	}
	
	private void initialize() {
		handlers.add(new KeyMapping(13, new ZoomIn()));
		handlers.add(new KeyMapping(Input.KEY_ADD, new ZoomIn()));
		handlers.add(new KeyMapping(Input.KEY_MINUS, new ZoomOut()));
		handlers.add(new KeyMapping(Input.KEY_SUBTRACT, new ZoomOut()));

		handlers.add(new KeyMapping(Input.KEY_UP, new ScrollUp()));
		handlers.add(new KeyMapping(Input.KEY_DOWN, new ScrollDown()));
		handlers.add(new KeyMapping(Input.KEY_LEFT, new ScrollLeft()));
		handlers.add(new KeyMapping(Input.KEY_RIGHT, new ScrollRight()));

		handlers.add(new KeyMapping(Input.KEY_W, new ScrollUp()));
		handlers.add(new KeyMapping(Input.KEY_S, new ScrollDown()));
		handlers.add(new KeyMapping(Input.KEY_A, new ScrollLeft()));
		handlers.add(new KeyMapping(Input.KEY_D, new ScrollRight()));
		
		handlers.add(new KeyMapping(Input.KEY_C, new ToggleRenderDebugInfo()));
		handlers.add(new KeyMapping(Input.KEY_V, new ToggleRenderMoreDebugInfo()));
		handlers.add(new KeyMapping(Input.KEY_P, new PauseToogle()));
		
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void inputStarted() {
		for (KeyMapping mapping : handlers) {
			if (mapping.pressed) {
				mapping._key.onKeyPressed();
			}
		}
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void setInput(Input arg0) {}

	@Override
	public void keyPressed(int keyCode, char c) {
		KeyMapping mapping = find(keyCode);
		if (mapping != null) {
			mapping.pressed = true;
		}
	}

	@Override
	public void keyReleased(int keyCode, char c) {
		KeyMapping mapping = find(keyCode);
		if (mapping != null) {
			mapping.pressed = false;
			mapping._key.onKeyReleased();
		}
	}

	private KeyMapping find(int keyCode) {
		for (KeyMapping mapping : handlers) {
			if (mapping._keyCode == keyCode) {
				return mapping;
			}
		}
		return null;
	}
	
	private static class KeyMapping {
		private int _keyCode;
		private InputKey _key;
		private boolean pressed;
		
		public KeyMapping(int keyCode, InputKey key) {
			_keyCode = keyCode;
			_key = key;
		}
	}

	private final class ZoomOut extends InputKey {
		@Override
		public void pressed() {
			_camera.zoomOut();
		}
	}

	private final class ZoomIn extends InputKey {
		@Override
		public void pressed() {
			_camera.zoomIn();
		}
	}

	private final class ScrollLeft extends InputKey {
		@Override
		public void pressed() {
			_camera.scrollLeft();
		}
	}

	private final class ScrollRight extends InputKey {
		@Override
		public void pressed() {
			_camera.scrollRight();
		}
	}

	private final class ScrollUp extends InputKey {
		@Override
		public void pressed() {
			_camera.scrollUp();
		}
	}

	private final class ScrollDown extends InputKey {
		@Override
		public void pressed() {
			_camera.scrollDown();
		}
	}
	private final class ToggleRenderDebugInfo extends InputKey {
		public ToggleRenderDebugInfo() {
			super(true);
		}
		@Override
		public void pressed() {
			_renderer.toggleRenderDebugInfo();
		}
	}
	private final class ToggleRenderMoreDebugInfo extends InputKey {
		public ToggleRenderMoreDebugInfo() {
			super(true);
		}
		@Override
		public void pressed() {
			_renderer.toggleRenderMoreDebugInfo();
		}
	}
	private final class PauseToogle extends InputKey {

		public PauseToogle() {
			super(true);
		}
		
		@Override
		public void pressed() {
			_gc.setPaused(!_gc.isPaused());
		}
		
	}
}
