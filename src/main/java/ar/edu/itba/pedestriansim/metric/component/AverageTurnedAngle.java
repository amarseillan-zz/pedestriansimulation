package ar.edu.itba.pedestriansim.metric.component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.PedestrianDynamicLineInfo;
import ar.edu.itba.pedestriansim.back.entity.PedestrianAreaFileSerializer.StaticFileLine;

import com.google.common.collect.Maps;

public class AverageTurnedAngle implements SimpleMetric {

	private final Map<Integer, PedestrianTurnedAngle> _turnedAngleById = Maps.newHashMap();

	@Override
	public void onIterationStart() {

	}

	@Override
	public void onIterationEnd() {

	}

	@Override
	public void update(float delta, PedestrianDynamicLineInfo pedestrian, StaticFileLine pedestrianStaticInfo) {
		PedestrianTurnedAngle value = _turnedAngleById.get(pedestrian.id());
		if (value == null) {
			_turnedAngleById.put(pedestrian.id(), value = new PedestrianTurnedAngle());
			value.velocity.set(pedestrian.velocity());
		} else {
			float deltaAngle = (float) (value.velocity.getTheta() - pedestrian.velocity().getTheta());
			deltaAngle = Math.abs(deltaAngle);
			if (deltaAngle > 180) {
				deltaAngle = 360 - deltaAngle;
			}
			value.angle += deltaAngle;
			value.velocity.set(pedestrian.velocity());
		}
	}

	@Override
	public void appendResults(FileWriter writer) throws IOException {
		writer.append(String.format("%.3f", average()));
	}

	private float average() {
		int pedestriansCount = _turnedAngleById.size();
		float toalTurnedAngle = sumTotalTurnedAngle();
		return toalTurnedAngle / pedestriansCount;
	}

	private float sumTotalTurnedAngle() {
		float totalTurnedAngleSum = 0;
		for (PedestrianTurnedAngle value : _turnedAngleById.values()) {
			totalTurnedAngleSum += value.angle;
		}
		return totalTurnedAngleSum;
	}

	private static class PedestrianTurnedAngle {
		final Vector2f velocity = new Vector2f();
		float angle = 0;
		
		@Override
		public String toString() {
			return angle + "";
		}
	}

	@Override
	public String name() {
		return "Angulo giro prom";
	}
}
