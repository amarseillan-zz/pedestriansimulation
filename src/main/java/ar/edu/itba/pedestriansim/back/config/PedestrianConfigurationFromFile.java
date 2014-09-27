package ar.edu.itba.pedestriansim.back.config;

import static java.util.Arrays.asList;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

import org.apache.commons.lang3.tuple.Pair;

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
		Pair<Float, Range<Float>> pedestrianAlphaBeta = Pair.of(getFloat("alpha"), getRange("beta"));
		Pair<Float, Float> wallAlphaBeta = Pair.of(getFloat("wallAlpha"), getFloat("wallBeta"));
		return new PedestrianAppConfig()
			.setTimeStep(new BigDecimal(get("timeStep")).setScale(5, RoundingMode.UP))
			.setExternalForceRadiusThreshold(getFloat("externalForceRadiusThreshold"))
			.setExternalForceThreshold(getFloat("externalForceThreshold"))
			.setStaticfile(new File(get("staticfile")))
			.setDynamicfile(new File(get("dynamicfile")))
			.setPedestrianFactory(new PedestrianFactory(mass, velocity, r, pedestrianAlphaBeta, wallAlphaBeta))
			.makeNewRun(getBoolean("makeNewRun"));
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
	
	private boolean getBoolean(String key) {
		String value = get(key);
		if (asList("true", "1").contains(value)) {
			return true;
		}
		if (asList("false", "0").contains(value)) {
			return false;
		}
		throw new IllegalArgumentException("Invalid boolean: " + value);
	}

}
