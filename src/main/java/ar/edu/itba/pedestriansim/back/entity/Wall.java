package ar.edu.itba.pedestriansim.back.entity;

import org.newdawn.slick.geom.Line;

public class Wall {

	private Line _thickBorder;
	private Line _line;

	public Wall(Line line) {
		_line = line;
	}

	public Wall setThickBorder(Line extraBorder) {
		_thickBorder = extraBorder;
		return this;
	}

	public Line line() {
		return _line;
	}

	public Line thickBorder() {
		return _thickBorder;
	}

	public boolean isThick() {
		return _thickBorder != null;
	}
}
