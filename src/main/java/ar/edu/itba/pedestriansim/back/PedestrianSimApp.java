package ar.edu.itba.pedestriansim.back;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import ar.edu.itba.pedestriansim.back.config.CrossingConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAppConfig;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.PedestrianForces;
import ar.edu.itba.pedestriansim.back.entity.PedestrianSim;
import ar.edu.itba.pedestriansim.back.factory.PedestrianForcesFactory;
import ar.edu.itba.pedestriansim.back.logic.FutureForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.logic.FuturePositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.logic.PedestrianAreaStateFileWriter;
import ar.edu.itba.pedestriansim.back.logic.PedestrianAreaStep;
import ar.edu.itba.pedestriansim.back.logic.PedestrianForceUpdaterComponent;
import ar.edu.itba.pedestriansim.back.logic.PedestrianPositionUpdaterComponent;
import ar.edu.itba.pedestriansim.back.logic.RemovePedestriansOnTarget;
import ar.edu.itba.pedestriansim.back.logic.ProducePedestrians;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.io.Closer;

public class PedestrianSimApp implements Runnable {

	public static void main(String[] args) {
		new PedestrianSimApp(new CrossingConfig().get()).run();
	}

	private static final Logger logger = Logger.getLogger(PedestrianSimApp.class);

	private final PedestrianAppConfig _config;

	public PedestrianSimApp(PedestrianAppConfig config) {
		_config = Preconditions.checkNotNull(config);
	}

	@Override
	public void run() {
		logger.info("[Simulation] started");
		configure(new PedestrianSim(_config.pedestrianArea())).run();
		logger.info("[Simulation] Finished");
	}

	private PedestrianSim configure(PedestrianSim sim) {
		final Closer fileCloser = Closer.create();
		FileWriter staticWriter = fileCloser.register(newFileWriter(_config.staticfile()));
		FileWriter dynamicWriter = fileCloser.register(newFileWriter(_config.dynamicfile()));
		final PedestrianForces forces = new PedestrianForcesFactory().build(_config);
		return sim
			.cutCondition(new Predicate<PedestrianArea>() {
				@Override
				public boolean apply(PedestrianArea input) {
					return input.elapsedTime().floatValue() > 20;
				}
			})
			.onStep(new PedestrianAreaStateFileWriter(staticWriter, dynamicWriter, 0))
			.onStep(new FutureForceUpdaterComponent(forces))
			.onStep(new FuturePositionUpdaterComponent())
			.onStep(new PedestrianForceUpdaterComponent(forces))
			.onStep(new PedestrianPositionUpdaterComponent())
			.onStep(new RemovePedestriansOnTarget())
			.onStep(new ProducePedestrians(_config.pedestrianFactory()))
			.onStep(new PedestrianAreaStep() {
				@Override
				public void update(PedestrianArea input) {
					input.addElapsedTime(input.timeStep());
				}
			})
			.onEnd(new Function<PedestrianArea, PedestrianArea>() {
				@Override
				public PedestrianArea apply(PedestrianArea input) {
					try {
						fileCloser.close();
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
					return input;
				}
			})
		;
	}

	private FileWriter newFileWriter(File file) {
		try {
			return new FileWriter(file);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
