package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class Pedestrians {

	public static final Predicate<Pedestrian> not(Pedestrian pedestrian) {
		return new SkipPedetrian(pedestrian);
	}

	public static final Function<Pedestrian, Vector2f> getFutureLocation() {
		return new Function<Pedestrian, Vector2f>() {
			@Override
			public Vector2f apply(Pedestrian input) {
				return input.getFuture().getBody().getCenter();
			}
		};
	}

	public static final Function<Pedestrian, Vector2f> getLocation() {
		return new Function<Pedestrian, Vector2f>() {
			@Override
			public Vector2f apply(Pedestrian input) {
				return input.getBody().getCenter();
			}
		};
	}

	private static final class SkipPedetrian implements Predicate<Pedestrian> {

		private Pedestrian _pedestrian;

		public SkipPedetrian(Pedestrian pedestrian) {
			_pedestrian = pedestrian;
		}

		@Override
		public boolean apply(Pedestrian input) {
			return !_pedestrian.equals(input);
		}

	}

}
