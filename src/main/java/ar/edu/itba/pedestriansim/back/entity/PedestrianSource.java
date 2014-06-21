package ar.edu.itba.pedestriansim.back.entity;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.GaussianRandomGenerator;
import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;

public class PedestrianSource {

	private RandomGenerator _produceDelayGenerator = new GaussianRandomGenerator(3, 2);
	private RandomGenerator _pedestrianAmountGenerator = new UniformRandomGenerator(5, 9);
	private Vector2f _center;
	private float _radius;
	private PedestrianMision _mission;
	private int _team;
	private int _totalProduced, _produceLimit;
	private boolean _enabled;

	public PedestrianSource(Vector2f center, float radius, PedestrianMision mission, int team) {
		_center = center;
		_radius = radius;
		_mission = mission;
		_team = team;
		_totalProduced = 0;
		_produceLimit = -1;
		setEnabled(true);
	}

	public PedestrianSource setProduceLimit(int produceLimit) {
		_produceLimit = produceLimit;
		return this;
	}

	public PedestrianSource setProduceDelayGenerator(RandomGenerator produceDelayGenerator) {
		_produceDelayGenerator = produceDelayGenerator;
		return this;
	}

	public PedestrianSource setPedestrianAmountGenerator(RandomGenerator pedestrianAmountGenerator) {
		_pedestrianAmountGenerator = pedestrianAmountGenerator;
		return this;
	}

	public void setEnabled(boolean enabled) {
		_enabled = enabled;
	}

	public boolean isEnabled() {
		return _enabled;
	}

	public Vector2f center() {
		return _center;
	}

	public float radius() {
		return _radius;
	}

	public int produceLimit() {
		return _produceLimit;
	}

	public int totalProduced() {
		return _totalProduced;
	}

	public void incTotalProduced(int delta) {
		_totalProduced += delta;
	}

	public int team() {
		return _team;
	}

	public RandomGenerator delay() {
		return _produceDelayGenerator;
	}

	public RandomGenerator amount() {
		return _pedestrianAmountGenerator;
	}

	public PedestrianMision mission() {
		return _mission;
	}
}
