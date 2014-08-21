package ar.edu.itba.pedestriansim.back.entity.spatial;

import java.util.Set;

import org.newdawn.slick.geom.Rectangle;

import com.google.common.collect.Sets;

public class Grid<T> {

	private final Rectangle _area;
	private final Set<T> _entities = Sets.newHashSet();

	public Grid(Rectangle agrea) {
		_area = agrea;
	}

	public Rectangle getArea() {
		return _area;
	}
	
	public Set<T> getEntities() {
		return _entities;
	}
	
	public void clear() {
		_entities.clear();
	}
	
	public int size() {
		return _entities.size();
	}
}
