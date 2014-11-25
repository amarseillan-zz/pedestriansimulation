package ar.edu.itba.pedestriansim.back.entity;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public final class Wall {

	private Line _line;
	private Optional<Vector2f> _normal = Optional.absent();

	public Wall(Line line) {
		_line = line;
	}

	public Wall setThickDirection(Vector2f normal) {
		Preconditions.checkArgument(Math.abs(new Vector2f(line().getNormal(0)).dot(normal)) == 1);
		_normal = Optional.of(normal);
		return this;
	}

	public Line line() {
		return _line;
	}

	public Optional<Vector2f> normal() {
		return _normal;
	}

	public boolean isThick() {
		return normal().isPresent();
	}
}
