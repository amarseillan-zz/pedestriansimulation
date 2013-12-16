package ar.edu.itba.pedestriansim;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ar.edu.itba.pedestriansim.back.PedestrianArea;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Configuration
@PropertySource("classpath:sim-setup.properties")
public class PedestrianAppConfig {

	@Autowired
	private Environment env;

    public PedestrianArea getPedestrianArea() {
    	int size = env.getProperty("grid.size", Integer.class);
    	int width = env.getProperty("grid.wAmount", Integer.class);
    	int height = env.getProperty("grid.hAmount", Integer.class);
    	return new PedestrianArea(width, height, size);
    }
    
    public List<Properties> getPedestrianSources() throws IOException {
    	List<Properties> sources = Lists.newArrayList();
    	String sourceString = Strings.nullToEmpty(env.getProperty("source"));
    	ClassLoader loader = getClass().getClassLoader();
    	for (String fileName : Arrays.asList(sourceString.split(","))) {
    		Properties property = new Properties();
    		InputStream stream = loader.getResourceAsStream(fileName.trim());
			property.load(stream);
    		stream.close();
    		sources.add(property);
    	}
    	return sources;
    }
    
    public InputStream getObjectFile() {
    	String sourceString = Strings.nullToEmpty(env.getProperty("objects"));
    	if (StringUtils.isEmpty(sourceString)) {
    		return null;
    	}
    	return getClass().getClassLoader().getResourceAsStream(sourceString);
    }
    
    public Optional<String[]> getLocation() {
    	String[] values = Strings.nullToEmpty(env.getProperty("location")).split(",");
    	Optional<String[]> location;
    	if (values.length == 0 || Strings.isNullOrEmpty(values[0])) {
    		location = Optional.absent();
    	} else {
    		location = Optional.of(values);
    	}
    	return location;
    }

    public String get(String name) {
    	return env.getProperty(name); 
    }

    public <T> T get(String name, Class<T> clazz) {
    	return env.getProperty(name, clazz);
    }

    public <T extends Enum<T>> T getEnum(Class<T> clazz) {
    	return get(clazz.getSimpleName(), clazz);
    }

    public <T extends Enum<T>, F> F getEnumParam(T enumType, String param, Class<F> type) {
    	return get(enumType.name() + "." + param, type);
    }

    public <T> Optional<T> getOptional(String name, Class<T> clazz) {
    	return Optional.fromNullable(get(name, clazz));
    }
}
