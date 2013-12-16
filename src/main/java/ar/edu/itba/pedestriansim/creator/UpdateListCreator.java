package ar.edu.itba.pedestriansim.creator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.pedestriansim.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.PedestrianArea;
import ar.edu.itba.pedestriansim.back.Updateable;
import ar.edu.itba.pedestriansim.back.component.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.GridPedestrianPositionUpdater;
import ar.edu.itba.pedestriansim.back.component.PedestrianPositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianRemoverComponent;

import com.google.common.collect.Lists;

@Component
public class UpdateListCreator {

	@Autowired
	private PedestrianAppConfig config;

	public List<Updateable> produce(PedestrianArea pedestrianArea) {
		List<Updateable> components = Lists.newLinkedList();
		components.add(new FutureForceUpdaterComponentCreator(config).produce(pedestrianArea));
		components.add(new FuturePositionUpdaterComponent(pedestrianArea));
		components.add(new PedestrianForceUpdaterCreator(config).produce(pedestrianArea));
		components.add(new PedestrianPositionUpdaterComponent(pedestrianArea));
		components.add(new PedestrianRemoverComponent(pedestrianArea));
		components.add(new GridPedestrianPositionUpdater(pedestrianArea));
		return components;
	}
}
