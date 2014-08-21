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

	private float _alpha;
	private float _beta;
	private float _wallAlpha;
	private float _wallBeta;
	private float externalForceRadiusThreshold;
	private float externalForceThreshold;

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

	public PedestrianAppConfig setAlpha(float alpha) {
		_alpha = alpha;
		return this;
	}

	public float alpha() {
		return _alpha;
	}

	public PedestrianAppConfig setBeta(float beta) {
		_beta = beta;
		return this;
	}

	public float beta() {
		return _beta;
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

	public PedestrianAppConfig setWallAlpha(float wallAlpha) {
		_wallAlpha = wallAlpha;
		return this;
	}

	public float wallAlpha() {
		return _wallAlpha;
	}

	public PedestrianAppConfig setWallBeta(float wallBeta) {
		_wallBeta = wallBeta;
		return this;
	}

	public float wallBeta() {
		return _wallBeta;
	}
}
