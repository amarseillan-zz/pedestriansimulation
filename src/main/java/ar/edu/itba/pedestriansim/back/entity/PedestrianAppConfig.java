package ar.edu.itba.pedestriansim.back.entity;

import java.io.File;
import java.math.BigDecimal;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.factory.PedestrianFactory;

public class PedestrianAppConfig {

	private PedestrianFactory pedestrianFactory;
	private PedestrianArea _pedestrianArea = new PedestrianArea();
	private File _staticfile;
	private File _dynamicfile;

	private Vector2f _areaDimention;

	private float externalForceRadiusThreshold;
	private float externalForceThreshold;
	private float _simulationTime;

	private boolean _makeNewRun;
	private String _outDir;

	public PedestrianArea pedestrianArea() {
		return _pedestrianArea;
	}

	public PedestrianAppConfig setStaticfile(File staticfile) {
		_staticfile = staticfile;
		return this;
	}

	public File staticfile() {
		return _staticfile;
	}

	public PedestrianAppConfig setDynamicfile(File dynamicfile) {
		_dynamicfile = dynamicfile;
		return this;
	}

	public File dynamicfile() {
		return _dynamicfile;
	}

	public PedestrianAppConfig setAreaDimention(Vector2f areaDimention) {
		_areaDimention = areaDimention;
		return this;
	}

	public Vector2f areaDimention() {
		return _areaDimention;
	}

	public PedestrianAppConfig setPedestrianFactory(PedestrianFactory pedestrianFactory) {
		this.pedestrianFactory = pedestrianFactory;
		return this;
	}

	public PedestrianFactory pedestrianFactory() {
		return pedestrianFactory;
	}

	public PedestrianAppConfig setExternalForceRadiusThreshold(float externalForceRadiusThreshold) {
		this.externalForceRadiusThreshold = externalForceRadiusThreshold;
		return this;
	}

	public PedestrianAppConfig setExternalForceThreshold(float externalForceThreshold) {
		this.externalForceThreshold = externalForceThreshold;
		return this;
	}

	public float getExternalForceRadiusThreshold() {
		return externalForceRadiusThreshold;
	}

	public float getExternalForceThreshold() {
		return externalForceThreshold;
	}

	public PedestrianAppConfig setTimeStep(BigDecimal timeStep) {
		pedestrianArea().setTimeStep(timeStep);
		return this;
	}

	public PedestrianAppConfig setSimulationTime(float simulationTime) {
		_simulationTime = simulationTime;
		return this;
	}

	public float simulationTime() {
		return _simulationTime;
	}

	public PedestrianAppConfig makeNewRun(boolean makeNewRun) {
		_makeNewRun = makeNewRun;
		return this;
	}

	public boolean isMakeNewRun() {
		return _makeNewRun;
	}

	public PedestrianAppConfig setOutDir(String outDir) {
		_outDir = outDir;
		return this;
	}

	public String outDir() {
		return _outDir;
	}

}
