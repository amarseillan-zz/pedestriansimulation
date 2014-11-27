package ar.edu.itba.pedestriansim.back.factory;

import org.apache.commons.lang3.tuple.Pair;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;

import com.google.common.collect.Range;

public class PedestrianFactory {

	private int lastId = 0;
	private final RandomGenerator _massGenerator;
	private final RandomGenerator _velocityGenerator;
	private final RandomGenerator _radiusGenerator;

	private float _pedestrianAlpha;
	private RandomGenerator _pedestrianBetaGenerator;

	private float _wallAlpha;
	private float _wallBeta;
	
	private float _futurePedestrianAlpha;
	private float _futurePedestrianBeta;

	public PedestrianFactory(Range<Float> mass, Range<Float> velocity, Range<Float> r, Pair<Float, Range<Float>> pedestrianAlphaBeta,
			Pair<Float, Float> wallAlphaBeta, Pair<Float, Float> futurePedestrianAlphaBeta) {
		_massGenerator = new UniformRandomGenerator(mass);
		_velocityGenerator = new UniformRandomGenerator(velocity);
		_radiusGenerator = new UniformRandomGenerator(r);
		setPedestrianAlphaBeta(pedestrianAlphaBeta.getLeft(), pedestrianAlphaBeta.getRight());
		setWallAlphaBeta(wallAlphaBeta.getLeft(), wallAlphaBeta.getRight());
		setFuturePedestrianAlphaBeta(futurePedestrianAlphaBeta.getLeft(), futurePedestrianAlphaBeta.getRight());
	}

	public PedestrianFactory setPedestrianAlphaBeta(float alpha, Range<Float> beta) {
		_pedestrianAlpha = alpha;
		_pedestrianBetaGenerator = new UniformRandomGenerator(beta);
		return this;
	}

	public PedestrianFactory setWallAlphaBeta(float alpha, Float beta) {
		_wallAlpha = alpha;
		_wallBeta = beta;
		return this;
	}
	
	public PedestrianFactory setFuturePedestrianAlphaBeta(float alpha, float beta) {
		_futurePedestrianAlpha = alpha;
		_futurePedestrianBeta = beta;
		return this;
	}

	public Pedestrian build(Vector2f location, int team, PedestrianMision mission) {
		RigidBody body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		Pedestrian pedestrian = new Pedestrian(lastId++, team, body);
		pedestrian.pedestrianRepulsionForceValues().setAlpha(_pedestrianAlpha).setBeta(_pedestrianBetaGenerator.generate());
		pedestrian.wallRepulsionForceValues().setAlpha(_wallAlpha).setBeta(_wallBeta);
		pedestrian.futurePedestrianRepulsionForceValues().setAlpha(_futurePedestrianAlpha).setBeta(_futurePedestrianBeta);
		pedestrian.setMission(mission.clone());
		pedestrian.setMaxVelocity(_velocityGenerator.generate());
		return pedestrian;
	}
}
