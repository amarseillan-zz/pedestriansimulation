package ar.edu.itba.pedestriansim.factory;

import java.util.List;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.PedestrianForces;
import ar.edu.itba.pedestriansim.back.component.Componenent;
import ar.edu.itba.pedestriansim.back.component.FutureForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.GridPedestrianPositionUpdater;
import ar.edu.itba.pedestriansim.back.component.PedestrianForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianPositionUpdaterComponent;

import com.google.common.collect.Lists;

public class ComponentFactory {

	private PedestrianAppConfig _config;

	public ComponentFactory(PedestrianAppConfig config) {
		_config = config;
	}

	public List<Componenent> produce(PedestrianArea pedestrianArea) {
		PedestrianForces pedestrianForces = new PedestrianForcesFactory(_config).produce();
		List<Componenent> components = Lists.newLinkedList();
		components.add(new FutureForceUpdaterComponent(pedestrianArea, pedestrianForces));
		components.add(new FuturePositionUpdaterComponent(pedestrianArea));
		components.add(new PedestrianForceUpdaterComponent(pedestrianArea, pedestrianForces));
		components.add(new PedestrianPositionUpdaterComponent(pedestrianArea));
//		components.add(new PedestrianRemoverComponent(pedestrianArea));
		components.add(new GridPedestrianPositionUpdater(pedestrianArea));
		return components;
	}
	
}
