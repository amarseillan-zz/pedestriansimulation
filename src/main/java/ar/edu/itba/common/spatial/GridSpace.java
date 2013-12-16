package ar.edu.itba.common.spatial;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GridSpace<T> {

	private static final Logger logger = Logger.getLogger(GridSpace.class);

	private static final int[][] neighbors = new int[][] {{0,-1}, {-1,0}, {1,0}, {1,0}};
	
	private final Grid<T>[][] _allGrids;
	private final Map<T, Set<Grid<T>>> _entitiesByGrid = Maps.newHashMap();
	private final Function<T, Shape> _boundaryGetter;
	
	private final int _gridSize;

	@SuppressWarnings("unchecked")
	public GridSpace(int gridsWith, int gridsHeight, int gridSize, Function<T, Shape> boundaryGetter) {
		_boundaryGetter = boundaryGetter;
		_gridSize = gridSize;
		_allGrids = new Grid[gridsWith][];
		for (int i = 0; i < gridsWith; i++) {
			_allGrids[i] = new Grid[gridsHeight];
			float x = i * gridSize;
			for (int j = 0; j < gridsHeight; j++) {
				float y = j * gridSize; 
				_allGrids[i][j] = new Grid<T>(new Rectangle(x, y, gridSize, gridSize));
			}
		}
	}

	public void clear() {
		for (Grid<T>[] row : _allGrids) {
			for (Grid<T> grid : row) {
				grid.clear();
			}
		}
		for (Set<Grid<T>> grids : _entitiesByGrid.values()) {
			grids.clear();
		}
	}

	public void add(T entity) {
		try {
			Set<Grid<T>> gridForEntity = _entitiesByGrid.get(entity);
			if (gridForEntity == null) {
				_entitiesByGrid.put(entity, gridForEntity = Sets.newHashSet());
			}
			Shape boundary = _boundaryGetter.apply(entity);
			int i = (int) (boundary.getCenterX() / _gridSize);
			int j = (int) (boundary.getCenterY() / _gridSize);
			Grid<T> grid = _allGrids[i][j];
			grid.getEntities().add(entity);
			gridForEntity.add(grid);
			if (!grid.getArea().contains(boundary)) {
				for (int[] neighbor : neighbors) {
					int x = i + neighbor[0];
					int y = j + neighbor[1];
					if (0 <= x && x < _allGrids.length && 0 <= y && y <= _allGrids[0].length) {
						Grid<T> neighboardGrid = _allGrids[x][y];
						if (neighboardGrid.getArea().intersects(boundary)) {
							neighboardGrid.getEntities().add(entity);
							gridForEntity.add(neighboardGrid);
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			logger.error("Entity out of bounds: " + entity, e);
		}
	}

	public Collection<T> getPossibleCollitions(T entity, Collection<T> result) {
		Set<Grid<T>> collitions = _entitiesByGrid.get(entity);
		result.clear();
		if (collitions != null) {
			for (Grid<T> grid : _entitiesByGrid.get(entity)) {
				result.addAll(grid.getEntities());
			}
		}
		return result;
	}

	public Grid<T>[][] getGrid() {
		return _allGrids;
	}

}
