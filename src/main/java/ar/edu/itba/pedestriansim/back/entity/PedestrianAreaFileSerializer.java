package ar.edu.itba.pedestriansim.back.entity;

import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.geom.Vector2f;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

public class PedestrianAreaFileSerializer {

	private final static String SPACE = " ";
	private final static String COMMA = ",";

	public Supplier<StaticFileLine> staticFileInfo(final Scanner scanner) {
		return new Supplier<StaticFileLine>() {
			@Override
			public StaticFileLine get() {
				StaticFileLine line = null;
				if (scanner.hasNextLine()) {
					String[] columns = scanner.nextLine().split(COMMA);
					if (columns.length == 0) {
						return null;
					}
					line = new StaticFileLine(Integer.valueOf(columns[0]), Integer.valueOf(columns[1]), Float.valueOf(columns[2]), Float.valueOf(columns[3]));
				}
				return line;
			}
		};
	}

	public Supplier<DymaimcFileStep> steps(final Scanner scanner) {
		return new Supplier<DymaimcFileStep>() {
			@Override
			public DymaimcFileStep get() {
				DymaimcFileStep line = null;
				if (scanner.hasNextLine()) {
					line = new DymaimcFileStep(Float.valueOf(scanner.nextLine()));
					while (scanner.hasNextLine() && !scanner.hasNextFloat()) {
						String[] columns = scanner.nextLine().split(COMMA);
						int id = Integer.valueOf(columns[0]);
						String[] centerString = columns[1].split(SPACE);
						Vector2f center = new Vector2f(Float.valueOf(centerString[0]), Float.valueOf(centerString[1]));
						// TODO: unharcode this
						String[] velocityString = columns[2].split(SPACE);
						Vector2f velocity = new Vector2f(Float.valueOf(velocityString[0]), Float.valueOf(velocityString[1]));
						String[] futureCenterString = columns[3].split(SPACE);
						Vector2f futureCenter = new Vector2f(Float.valueOf(futureCenterString[0]), Float.valueOf(futureCenterString[1]));
						PedestrianDynamicLineInfo lineInfo = new PedestrianDynamicLineInfo(id, center, velocity, futureCenter);
						line.pedestriansInfo().add(lineInfo);
					}
				}
				return line;
			}
		};
	}

	public static final class StaticFileLine {
		private int _id;
		private int _team;
		private float _radius;
		private float _mass;

		public StaticFileLine(int id, int team, float mass, float radius) {
			_id = id;
			_team = team;
			_radius = radius;
			_mass = mass;
		}

		public int id() {
			return _id;
		}

		public int team() {
			return _team;
		}

		public float radius() {
			return _radius;
		}

		public float mass() {
			return _mass;
		}
	}

	public static final class DymaimcFileStep {
		private float _step;
		private List<PedestrianDynamicLineInfo> _pedestrialsInfo = Lists.newLinkedList();

		public DymaimcFileStep(float step) {
			_step = step;
		}

		public float step() {
			return _step;
		}

		public List<PedestrianDynamicLineInfo> pedestriansInfo() {
			return _pedestrialsInfo;
		}

	}

	public static class PedestrianDynamicLineInfo {
		private int _id;
		private Vector2f _center;
		private Vector2f _velocity;
		private Vector2f _futureCenter;

		public PedestrianDynamicLineInfo(int id, Vector2f center, Vector2f velocity, Vector2f futureCenter) {
			_id = id;
			_center = center;
			_velocity = velocity;
			_futureCenter = futureCenter;
		}

		public int id() {
			return _id;
		}

		public Vector2f center() {
			return _center;
		}

		public Vector2f velocity() {
			return _velocity;
		}

		public Vector2f futureCenter() {
			return _futureCenter;
		}
	}

}
