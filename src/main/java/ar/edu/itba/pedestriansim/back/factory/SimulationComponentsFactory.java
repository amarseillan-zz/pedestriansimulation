package ar.edu.itba.pedestriansim.back.factory;

import java.util.List;

import ar.edu.itba.pedestriansim.back.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.component.Component;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;

public interface SimulationComponentsFactory {

	List<Component> produce(PedestrianAppConfig config, PedestrianArea pedestrianArea);

}
