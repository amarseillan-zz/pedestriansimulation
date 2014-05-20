package ar.edu.itba.pedestriansim.back;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.edu.itba.pedestriansim.back.PedestrianAppConfig.OptionalConfig;
import ar.edu.itba.pedestriansim.back.component.Component;
import ar.edu.itba.pedestriansim.back.component.FutureForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.GridPedestrianPositionUpdater;
import ar.edu.itba.pedestriansim.back.component.PedestrianAreaStateFileWriter;
import ar.edu.itba.pedestriansim.back.component.PedestrianForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianPositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.component.PedestrianRemoverComponent;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSim;
import ar.edu.itba.pedestriansim.back.factory.PedestrianForcesFactory;
import ar.edu.itba.pedestriansim.back.factory.SimulationComponentsFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

@org.springframework.stereotype.Component
public class PedestrianSimApp {

	public static void main(String[] args) {
		if (args.length < 2 ){
			throw new IllegalArgumentException("Too few arguments, you should at least state dynamicfile and staticfile");
		}
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		context.refresh();
		PedestrianSimApp simulation = context.getBean(PedestrianSimApp.class);
		OptionalConfig config = null;
		if (args.length == 5) {
			
			float externalForceThreshold = Float.valueOf(args[2]);
			float alpha = Float.valueOf(args[3]);
			float beta = Float.valueOf(args[4]);
			config = new OptionalConfig(externalForceThreshold, alpha, beta);
		} 
		simulation.run(config, args[0], args[1]);
		context.close();
	}

	private static final Logger logger = Logger
			.getLogger(PedestrianSimApp.class);

	@Autowired
	private PedestrianAppConfig _config;

	public void run(OptionalConfig config, String staticfile, String dynamicfile) {
		logger.info("Loading simulation...");
		Predicate<PedestrianArea> cutCondition = new Predicate<PedestrianArea>() {
			@Override
			public boolean apply(PedestrianArea input) {
				return input.elapsedTime().floatValue() > 20;
			}
		};
		_config.setOptional(config);
		_config.setStaticfile(staticfile);
		_config.setDynamicfile(dynamicfile);
		PedestrianSim simulation = new PedestrianSim(_config,
				new SimulationComponentsFactoryImpl(), cutCondition);
		logger.info("Starting simulation...");
		simulation.start();
		do {
			simulation.update(simulation.getPedestrianArea().delta());
		} while (!simulation.isFinished());
		logger.info("Simulation finished OK!");
	}

	public static class SimulationComponentsFactoryImpl implements
			SimulationComponentsFactory {

		public List<Component> produce(PedestrianAppConfig config,
				PedestrianArea pedestrianArea) {
			PedestrianForces pedestrianForces = new PedestrianForcesFactory(
					config).produce();
			List<Component> components = Lists.newLinkedList();
			components.add(new FutureForceUpdaterComponent(pedestrianArea,
					pedestrianForces));
			components.add(new FuturePositionUpdaterComponent(pedestrianArea));
			components.add(new PedestrianForceUpdaterComponent(pedestrianArea,
					pedestrianForces));
			components.add(new PedestrianPositionUpdaterComponent(
					pedestrianArea));
			components.add(new PedestrianRemoverComponent(pedestrianArea));
			components.add(new GridPedestrianPositionUpdater(pedestrianArea));
			File outputDirectory = new File(config.get("log.directory"));
			float logInterval = config.get("log.interval", Float.class);
			components.add(new PedestrianAreaStateFileWriter(pedestrianArea,
					outputDirectory, logInterval, config.getStaticfile(), config.getDynamicfile()));
			return components;
		}

	}

}
