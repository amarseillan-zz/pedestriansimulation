package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Carrot {

	private Shape shape;

	public Carrot(Vector2f position) {
		this.shape = new Circle(position.x, position.y, 3);
	}

	public Shape getShape() {
		return shape;
	}

	public void apply(Transform transform) {
		shape = shape.transform(transform);
	}
}
