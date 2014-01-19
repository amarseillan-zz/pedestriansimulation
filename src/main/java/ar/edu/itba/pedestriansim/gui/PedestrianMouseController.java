package ar.edu.itba.pedestriansim.gui;

import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.mision.PedestrianTargetArea;

import com.google.common.base.Preconditions;

public class PedestrianMouseController implements MouseListener {

	private Input _input;
	private final Vector2f mouseTransfomedLocation;
	private final Camera _camera;
	private final Pedestrian _pedestrian;

	public PedestrianMouseController(Pedestrian pedestrian, Camera camera) {
		_pedestrian = Preconditions.checkNotNull(pedestrian);
		_camera = Preconditions.checkNotNull(camera);
		mouseTransfomedLocation = new Vector2f();
	}
	
	@Override
	public void setInput(Input input) {
		_input = input;
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		Vector2f worldCoord = getMouseInWorldCoord();
		Vector2f  oldLocation = _pedestrian.getBody().getCenter();
		_pedestrian.translate(worldCoord.x - oldLocation.x, worldCoord.y - oldLocation.y);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		_pedestrian.getMission().clear();
		Vector2f worldCoord = getMouseInWorldCoord();
		PedestrianTargetArea newTarget = new PedestrianTargetArea(new Circle(worldCoord.x, worldCoord.y, 0.5f));
		_pedestrian.getMission().putFirst(newTarget);
	}

	@Override
	public void mouseWheelMoved(int change) {
	}

	private Vector2f getMouseInWorldCoord() {
		mouseTransfomedLocation.set(_input.getMouseX(), _input.getMouseY());
		return _camera.inverseTransform(mouseTransfomedLocation);
	}
}
