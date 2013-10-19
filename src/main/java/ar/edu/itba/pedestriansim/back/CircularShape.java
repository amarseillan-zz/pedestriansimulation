package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

public class CircularShape {

	private Pedestrian _owner;
	private Shape _shape = new Circle(0, 0, 10);

	public CircularShape(Pedestrian owner) {
		_owner = owner;
	}
	
	public boolean isCollidingWith(CircularShape other) {
		recenterShape();
		other.recenterShape();
		return _shape.intersects(other._shape) 
			|| _shape.contains(other._shape) || other._shape.contains(_shape);
	}
	
	private void recenterShape() {
		_shape.setLocation(_owner.getBody().getLocation());		
	}
}
