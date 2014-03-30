package ar.edu.itba.pedestriansim.back.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.physics.Collitions;
import ar.edu.itba.pedestriansim.back.spatial.GridSpace;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class PedestrianArea {

	private final List<Pedestrian> collitions = Lists.newLinkedList();

	private final GridSpace<Pedestrian> _map;
	private final List<Pedestrian> _pedestrians = Lists.newLinkedList();
	private final List<Shape> _obstacles = Lists.newArrayList();
	private final List<PedestrianSource> _sources = Lists.newArrayList();
	private BigDecimal _elapsedTime = BigDecimal.ZERO;

	public PedestrianArea(int width, int height, int gridSize) {
		_map = new GridSpace<>(width, height, gridSize, new Function<Pedestrian, Shape>() {
			public Shape apply(Pedestrian pedestrian) {
				return pedestrian.getShape();
			}
		});
	}

	public void addElapsedTime(float delta) {
		_elapsedTime = _elapsedTime.add(new BigDecimal(delta)).setScale(2, RoundingMode.HALF_DOWN);
	}

	public BigDecimal elapsedTime() {
		return _elapsedTime;
	}

	public GridSpace<Pedestrian> getMap() {
		return _map;
	}

	public void addPedestrian(Pedestrian pedestrian) {
		_pedestrians.add(pedestrian);
	}

	public void removePedestrians(Predicate<Pedestrian> predicate) {
		Iterables.removeIf(getPedestrians(), predicate);
	}

	public void removePedestrians(Collection<Pedestrian> pedestrians) {
		_pedestrians.removeAll(pedestrians);
	}

	public Collection<Pedestrian> getCollitions(Pedestrian pedestrian) {
		return _map.getPossibleCollitions(pedestrian, collitions);
	}

	public boolean hasCollitions(Pedestrian pedestrian) {
		for (Pedestrian possible : getCollitions(pedestrian)) {
			if (Collitions.touching(possible.getShape(), pedestrian.getShape())) {
				return false;
			}
		}
		// XXX: marse, acordate que me dijsite que no iban a chocar contra las paredes...
		return true;
	}

	public void addObstacle(Shape obstacte) {
		_obstacles.add(obstacte);
	}

	public void addSource(PedestrianSource source) {
		source.start();
		_sources.add(source);
	}

	public Collection<Shape> getObstacles() {
		return _obstacles;
	}

	public Collection<Pedestrian> getPedestrians() {
		return _pedestrians;
	}

	public Iterable<Pedestrian> getPedestriansAndSkip(Pedestrian pedestrian) {
		return Iterables.filter(_pedestrians, Pedestrians.not(pedestrian));
	}

	public Collection<PedestrianSource> getSources() {
		return _sources;
	}
}
