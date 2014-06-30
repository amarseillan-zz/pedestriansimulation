package ar.edu.itba.pedestriansim.back.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.factory.PedestrianFactory;

import com.google.common.collect.Range;

public class DefaultPedestrianAppConfig implements ApplicationConfigBuilder {

	@Override
	public PedestrianAppConfig get() {
		PedestrianAppConfig config = new PedestrianAppConfig()
			.setTimeStep(new BigDecimal(1 / 100f).setScale(5, RoundingMode.UP))
			.setAlpha(800)
			.setBeta(0.5f)
			.setExternalForceRadiusThreshold(0)
			.setExternalForceThreshold(0)
			.setStaticfile(new File("static.txt"))
			.setDynamicfile(new File("dynamic.txt"))
			;
		Range<Float> mass = Range.closed(60f, 80f);
		Range<Float> velocity = Range.closed(1.4f, 1.6f);
		Range<Float> r = Range.closed(0.25f, 0.29f);
		config.setPedestrianFactory(new PedestrianFactory(mass, velocity, r));
		return config;
	}
}
