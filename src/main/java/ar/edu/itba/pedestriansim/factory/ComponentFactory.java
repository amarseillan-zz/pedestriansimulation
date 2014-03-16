package ar.edu.itba.pedestriansim.factory;

import java.io.File;
import java.util.List;

import org.springframework.util.StringUtils;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianForces;
import ar.edu.itba.pedestriansim.back.component.Component;
import ar.edu.itba.pedestriansim.back.component.FutureForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.GridPedestrianPositionUpdater;
import ar.edu.itba.pedestriansim.back.component.MetricsComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianAreaStateFileWriter;
import ar.edu.itba.pedestriansim.back.component.PedestrianForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianPositionUpdaterComponent;

import com.google.common.collect.Lists;

public class ComponentFactory {

	private PedestrianAppConfig _config;

	public ComponentFactory(PedestrianAppConfig config) {
		_config = config;
	}

	public List<Component> produce(PedestrianArea pedestrianArea) {
		PedestrianForces pedestrianForces = new PedestrianForcesFactory(_config).produce();
		List<Component> components = Lists.newLinkedList();
		components.add(new FutureForceUpdaterComponent(pedestrianArea, pedestrianForces));
		components.add(new FuturePositionUpdaterComponent(pedestrianArea));
		components.add(new PedestrianForceUpdaterComponent(pedestrianArea, pedestrianForces));
		components.add(new PedestrianPositionUpdaterComponent(pedestrianArea));
//		components.add(new PedestrianRemoverComponent(pedestrianArea));
		components.add(new GridPedestrianPositionUpdater(pedestrianArea));
		String outputFile = _config.get("log.file");
		String metricFile = _config.get("metrics.file");
		if (!StringUtils.isEmpty(outputFile)) {
			float stepInterval = _config.get("log.stepInterval", Float.class);
			components.add(new PedestrianAreaStateFileWriter(pedestrianArea, new File(outputFile), stepInterval));
			components.add(new MetricsComponent(pedestrianArea, new File(metricFile)));
		}
		return components;
	}
	
}
