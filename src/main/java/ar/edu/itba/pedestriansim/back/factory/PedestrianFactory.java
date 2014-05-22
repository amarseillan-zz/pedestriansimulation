package ar.edu.itba.pedestriansim.back.factory;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.back.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.back.physics.RigidBody;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public class PedestrianFactory {

	private int lastId = 0;
	private final RandomGenerator _massGenerator;
	private final RandomGenerator _velocityGenerator;
	private final RandomGenerator _radiusGenerator;

	public PedestrianFactory(PedestrianAppConfig config) {
		_massGenerator = new UniformRandomGenerator(getRange("pedestrian.mass", config));
		_velocityGenerator = new UniformRandomGenerator(getRange("pedestrian.velocity", config));
		_radiusGenerator = new UniformRandomGenerator(getRange("pedestrian.radius", config));
	}
	
	private Range<Float> getRange(String name, PedestrianAppConfig _config) {
		String[] values = _config.get(name).split(" ");
		return Range.closed(
			Float.parseFloat(values[0].trim()), 
			Float.parseFloat(values[1].trim()));
	}

	public Pedestrian build(Vector2f location, int team, PedestrianMision mission) {
		RigidBody body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		Pedestrian pedestrian = new Pedestrian(lastId++, team, body);
		pedestrian.setMission(mission);
		pedestrian.setMaxVelocity(_velocityGenerator.generate());
		return pedestrian;
	}
}
