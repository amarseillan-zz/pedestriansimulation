package ar.edu.itba.pedestriansim.factory;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.Pedestrian;
import ar.edu.itba.pedestriansim.back.mision.PedestrianMision;
import ar.edu.itba.pedestriansim.physics.RigidBody;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public class PedestrianFactory {

	private int lastId = 0;
	private final RandomGenerator _massGenerator;
	private final RandomGenerator _velocityGenerator;
	private final RandomGenerator _radiusGenerator;
	private final Optional<Float> _reactionDistance;

	public PedestrianFactory(PedestrianAppConfig config) {
		_massGenerator = new UniformRandomGenerator(getRange("pedestrian.mass", config));
		_velocityGenerator = new UniformRandomGenerator(getRange("pedestrian.velocity", config));
		_radiusGenerator = new UniformRandomGenerator(getRange("pedestrian.radius", config));
		_reactionDistance = config.getOptional("pedestrian.reactionDistance", Float.class);
	}
	
	private Range<Float> getRange(String name, PedestrianAppConfig _config) {
		String[] values = _config.get(name).split(" ");
		return Range.closed(
			Float.parseFloat(values[0].trim()), 
			Float.parseFloat(values[1].trim()));
	}

	public Pedestrian build(Vector2f location, int team, PedestrianMision mission) {
		RigidBody body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		Pedestrian pedestrian = new Pedestrian(lastId++, mission, team, body);
		pedestrian.setMaxVelocity(_velocityGenerator.generate());
		if (_reactionDistance.isPresent()) {
			pedestrian.setReactionDistance(_reactionDistance.get());
		}
		return pedestrian;
	}
}
