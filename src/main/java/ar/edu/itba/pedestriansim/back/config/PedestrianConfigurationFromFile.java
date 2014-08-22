package ar.edu.itba.pedestriansim.back.config;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.factory.PedestrianFactory;

import com.google.common.collect.Range;

public class PedestrianConfigurationFromFile implements ApplicationConfigBuilder {

	private Properties _properties;

	public PedestrianConfigurationFromFile(Properties properties) {
		_properties = properties;
	}

	@Override
	public PedestrianAppConfig get() {
		Range<Float> mass = getRange("mass");
		Range<Float> velocity = getRange("velocity");
		Range<Float> r = getRange("r");
		return new PedestrianAppConfig()
			.setTimeStep(new BigDecimal(get("timeStep")).setScale(5, RoundingMode.UP))
			.setAlpha(getFloat("alpha"))
			.setBeta(getFloat("beta"))
			.setWallAlpha(getFloat("wallAlpha"))
			.setWallBeta(getFloat("wallBeta"))
			.setExternalForceRadiusThreshold(getFloat("externalForceRadiusThreshold"))
			.setExternalForceThreshold(getFloat("externalForceThreshold"))
			.setStaticfile(new File(get("staticfile")))
			.setDynamicfile(new File(get("dynamicfile")))
			.setPedestrianFactory(new PedestrianFactory(mass, velocity, r))
		;
	}

	private Range<Float> getRange(String key) {
		String[] values = get(key).split("-");
		return Range.closed(Float.valueOf(values[0].trim()), Float.valueOf(values[1].trim()));
	}

	private Float getFloat(String key) {
		return Float.valueOf(get(key));
	}

	private String get(String key) {
		return _properties.getProperty(key).trim();
	}

}
