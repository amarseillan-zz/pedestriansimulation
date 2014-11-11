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
		Range<Float> velocity = Range.closed(1.2f, 1.4f);
		Range<Float> r = Range.closed(0.25f, 0.29f);
		Pair<Float, Range<Float>> pedestrianAlphaBeta = Pair.of(1000f, Range.closed(0.65f, 0.75f));
		Pair<Float, Float> wallAlphaBeta = Pair.of(600f, .75f);
		Pair<Float, Float> futurePedestrianAlphaBeta = Pair.of(2000f, 0.2f);
		return new PedestrianAppConfig()
			.makeNewRun(true)
			.setTimeStep(new BigDecimal(1 / 500f).setScale(5, RoundingMode.UP))
			.setExternalForceRadiusThreshold(0)
			.setExternalForceThreshold(0)
			.setStaticfile(new File("static.txt"))
			.setDynamicfile(new File("dynamic.txt"))
			.setSimulationTime(100)
			.setPedestrianFactory(new PedestrianFactory(mass, velocity, r, pedestrianAlphaBeta, wallAlphaBeta, futurePedestrianAlphaBeta));
	}
}
