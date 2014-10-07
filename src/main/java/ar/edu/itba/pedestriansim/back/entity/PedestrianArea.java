package ar.edu.itba.pedestriansim.back.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.entity.physics.Collitions;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class PedestrianArea {

	private final List<Pedestrian> _pedestrians = Lists.newLinkedList();
	private final List<Shape> _obstacles = Lists.newArrayList();
	private final List<PedestrianSource> _sources = Lists.newArrayList();
	private BigDecimal _timeStep;
	private BigDecimal _elapsedTime = BigDecimal.ZERO;

	public void addElapsedTime(BigDecimal delta) {
		_elapsedTime = _elapsedTime.add(delta).setScale(4, RoundingMode.HALF_DOWN);
	}

	public void setTimeStep(BigDecimal timeStep) {
		_timeStep = timeStep;
	}

	public BigDecimal timeStep() {
		return _timeStep;
	}

	public BigDecimal elapsedTime() {
		return _elapsedTime;
	}

	public Collection<Pedestrian> pedestrians() {
		return _pedestrians;
	}

	public void addPedestrian(Pedestrian pedestrian) {
		_pedestrians.add(pedestrian);
	}

	public void removePedestrians(Collection<Pedestrian> pedestrians) {
		_pedestrians.removeAll(pedestrians);
	}

	public Iterable<Pedestrian> getPedestriansAndSkip(Pedestrian pedestrian) {
		return Iterables.filter(_pedestrians, Pedestrians.not(pedestrian));
	}

	public Collection<Shape> obstacles() {
		return _obstacles;
	}

	public void addObstacle(Shape obstacte) {
		_obstacles.add(obstacte);
	}

	public void addSource(PedestrianSource source) {
		_sources.add(source);
	}

	public Collection<PedestrianSource> sources() {
		return _sources;
	}

	public boolean hasCollitions(Pedestrian pedestrian) {
		// FIXME: usar alguna estructura espacial para optimizar la busqueda
		for (Pedestrian possible : _pedestrians) {
			if (possible != pedestrian && Collitions.touching(possible.getBody().getShape(), pedestrian.getBody().getShape())) {
				return true;
			}
		}
		return false;
	}
}
