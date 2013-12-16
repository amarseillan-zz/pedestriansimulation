package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;
import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.physics.RigidBody;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

@Component
public class PedestrianFactory {

	private int lastId = 0;
	private final RandomGenerator _massGenerator;
	private final RandomGenerator _velocityGenerator;
	private final RandomGenerator _radiusGenerator;
	private final Optional<Float> _reactionDistance;

	@Autowired
	public PedestrianFactory(PedestrianAppConfig _config) {
		_massGenerator = new UniformRandomGenerator(getRange("pedestrian.mass", _config));
		_velocityGenerator = new UniformRandomGenerator(getRange("pedestrian.velocity", _config));
		_radiusGenerator = new UniformRandomGenerator(getRange("pedestrian.radius", _config));
		_reactionDistance = _config.getOptional("pedestrian.reactionDistance", Float.class);
	}
	
	private Range<Float> getRange(String name, PedestrianAppConfig _config) {
		String[] values = _config.get(name).split(" ");
		return Range.closed(
			Float.parseFloat(values[0].trim()), 
			Float.parseFloat(values[1].trim()));
	}

	public Pedestrian build(Vector2f location, int team, PedestrianTargetList targets) {
		RigidBody body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		Pedestrian pedestrian = new Pedestrian(lastId++, targets, team, body);
		pedestrian.setMaxVelocity(_velocityGenerator.generate());
		if (_reactionDistance.isPresent()) {
			pedestrian.setReactionDistance(_reactionDistance.get());
		}
		return pedestrian;
	}
}
