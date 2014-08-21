package ar.edu.itba.pedestriansim.back.factory;

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

	public PedestrianFactory(Range<Float> mass, Range<Float> velocity, Range<Float> r) {
		_massGenerator = new UniformRandomGenerator(mass);
		_velocityGenerator = new UniformRandomGenerator(velocity);
		_radiusGenerator = new UniformRandomGenerator(r);
	}

	public Pedestrian build(Vector2f location, int team, PedestrianMision mission) {
		RigidBody body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		Pedestrian pedestrian = new Pedestrian(lastId++, team, body);
		pedestrian.setMission(mission.clone());
		pedestrian.setMaxVelocity(_velocityGenerator.generate());
		return pedestrian;
	}
}
