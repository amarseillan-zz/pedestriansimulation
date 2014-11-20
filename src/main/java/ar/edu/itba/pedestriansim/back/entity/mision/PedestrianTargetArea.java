package ar.edu.itba.pedestriansim.back.entity.mision;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

public class PedestrianTargetArea implements PedestrianTarget {

	private Line _targetArea;
	private Vector2f _result = new Vector2f();
	private int _id;

	public PedestrianTargetArea(Line targetArea) {
		_targetArea = targetArea;
	}

	public PedestrianTargetArea setId(int id) {
		_id = id;
		return this;
	}

	public int id() {
		return _id;
	}

	@Override
	public boolean intersects(Shape shape) {
		return _targetArea.intersects(shape);
	}

	@Override
	public Vector2f getClosesPoint(Vector2f point) {
		_targetArea.getClosestPoint(point, _result);
		return _result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
