package ar.edu.itba.pedestriansim.back.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.Scene;
import ar.edu.itba.pedestriansim.back.Updateable;

public class PositionUpdater implements Updateable {

	private Scene scene;

	public PositionUpdater(Scene scene) {
		this.scene = scene;
	}

	public void update(GameContainer gc, float elapsedTimeSeconds) {
		for (Pedestrian subject : scene.getPedestrians()) {
			Vector2f velocity = subject.getVelocity();
			Vector2f translation = velocity.scale(elapsedTimeSeconds); 
			float xOffset = translation.x;
			float yOffset = translation.y;
			subject.apply(Transform.createTranslateTransform(xOffset, yOffset));
		}
	}

}
