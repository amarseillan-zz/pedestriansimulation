package ar.edu.itba.pedestriansim.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.component.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.GridPedestrianPositionUpdater;
import ar.edu.itba.pedestriansim.back.component.PedestrianPositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianRemoverComponent;
import ar.edu.itba.pedestriansim.factory.component.FutureForceUpdaterComponentFactory;
import ar.edu.itba.pedestriansim.factory.component.PedestrianForceUpdaterFactory;

import com.google.common.collect.Lists;

public class ComponentFactory {

	@Autowired
	private PedestrianAppConfig _config;

	public ComponentFactory(PedestrianAppConfig config) {
		_config = config;
	}

	public List<Updateable> produce(PedestrianArea pedestrianArea) {
		List<Updateable> components = Lists.newLinkedList();
		components.add(new FutureForceUpdaterComponentFactory(_config).produce(pedestrianArea));
		components.add(new FuturePositionUpdaterComponent(pedestrianArea));
		components.add(new PedestrianForceUpdaterFactory(_config).produce(pedestrianArea));
		components.add(new PedestrianPositionUpdaterComponent(pedestrianArea));
		components.add(new PedestrianRemoverComponent(pedestrianArea));
		components.add(new GridPedestrianPositionUpdater(pedestrianArea));
		return components;
	}
}
