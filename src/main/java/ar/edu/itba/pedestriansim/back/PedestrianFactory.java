package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.common.rand.RandomGenerator;
import ar.edu.itba.common.rand.UniformRandomGenerator;

public class PedestrianFactory {

	private static final PedestrianFactory instance = new PedestrianFactory();

	private static final RandomGenerator _massGenerator = new UniformRandomGenerator(50, 80);
	private static final RandomGenerator _velocityGenerator = new UniformRandomGenerator(1f, 1.6f);
	private static final RandomGenerator _radiusGenerator = new UniformRandomGenerator(0.25f, 0.29f);

	public static PedestrianFactory instance() {
		return instance;
	}

	private int lastId = 0;

	public Pedestrian build(Vector2f location, int team, PedestrianTargetList targets) {
		RigidBody body = new RigidBody(_massGenerator.generate(), location, _radiusGenerator.generate());
		Pedestrian pedestrian = new Pedestrian(lastId++, targets, team, body);
		pedestrian.setMaxVelocity(_velocityGenerator.generate());
		return pedestrian;
	}
}
