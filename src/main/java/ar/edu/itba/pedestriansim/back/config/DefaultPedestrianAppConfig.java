package ar.edu.itba.pedestriansim.back.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.tuple.Pair;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.factory.PedestrianFactory;

import com.google.common.collect.Range;

public class DefaultPedestrianAppConfig implements ApplicationConfigBuilder {

	@Override
	public PedestrianAppConfig get() {
		Range<Float> mass = Range.closed(60f, 80f);
		Range<Float> velocity = Range.closed(1.3f, 1.4f);
		Range<Float> r = Range.closed(0.25f, 0.29f);
		Pair<Float, Range<Float>> pedestrianAlphaBeta = Pair.of(800f, Range.closed(0.65f, 0.85f));
		Pair<Float, Float> wallAlphaBeta = Pair.of(800f, .55f);
		return new PedestrianAppConfig()
			.makeNewRun(true)
			.setTimeStep(new BigDecimal(1 / 1000f).setScale(5, RoundingMode.UP))
			.setExternalForceRadiusThreshold(0)
			.setExternalForceThreshold(0)
			.setStaticfile(new File("static.txt"))
			.setDynamicfile(new File("dynamic.txt"))
			.setSimulationTime(70)
			.setPedestrianFactory(new PedestrianFactory(mass, velocity, r, pedestrianAlphaBeta, wallAlphaBeta));
	}
}
