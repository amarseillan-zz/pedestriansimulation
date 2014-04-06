package ar.edu.itba.pedestriansim.back.entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.geom.Vector2f;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

public class PedestrianAreaFileSerializer {

	private static final String STATIC_FILE_NAME = "static.txt";
	private static final String DYNAMIC_FILE_NAME = "dynamic.txt";
	
	private final static String LINE_BREAK = "\n";
	private final static String SPACE = " ";
	private final static String COMMA = ",";
	
	private Float delta;

	private File _directory;
	private PedestrianArea _pedestrianArea;

	public PedestrianAreaFileSerializer(PedestrianArea pedestrianArea, File directory) {
		//_pedestrianArea = Preconditions.checkNotNull(pedestrianArea);
		_pedestrianArea = pedestrianArea;
		_directory = directory;
	}
	
	public BufferedWriter createStaticFile() throws IOException {
		return new BufferedWriter(new FileWriter(buildPath(STATIC_FILE_NAME)));
	}
	
	public void saveSimulationTime(BufferedWriter staticFileWriter, float time) throws IOException{
		staticFileWriter.append(String.valueOf(time));
		staticFileWriter.append(LINE_BREAK);
	}

	public void saveStaticStep(BufferedWriter staticFileWriter, Pedestrian pedestrian) throws IOException {
		staticFileWriter.append(pedestrian.getId().toString());
		staticFileWriter.append(COMMA);
		staticFileWriter.append(pedestrian.getTeam() + "");
		staticFileWriter.append(COMMA);
		staticFileWriter.append(pedestrian.getBody().getMass() + "");
		staticFileWriter.append(COMMA);
		staticFileWriter.append(pedestrian.getBody().getRadius() + "");
		staticFileWriter.append(LINE_BREAK);
	}
	
	public Supplier<StaticFileLine> staticFileInfo() throws FileNotFoundException {
		final Scanner scanner = new Scanner(new File(buildPath(STATIC_FILE_NAME)));
		delta = Float.valueOf(scanner.nextLine());
		return new Supplier<StaticFileLine>() {
			@Override
			public StaticFileLine get() {
				StaticFileLine line = null;
				if (scanner.hasNextLine()) {
					String[] columns = scanner.nextLine().split(COMMA);
					line = new StaticFileLine(Integer.valueOf(columns[0]), Integer.valueOf(columns[1]),
						Float.valueOf(columns[2]), Float.valueOf(columns[3]));
				} else {
					scanner.close();
				}
				return line; 
			}
		};
	}

	public BufferedWriter createDynamicWriter() throws IOException {
		return new BufferedWriter(new FileWriter(buildPath(DYNAMIC_FILE_NAME)));
	}

	public void saveStep(BufferedWriter writer) throws IOException {
		BigDecimal step = _pedestrianArea.elapsedTime();
		writer.write(step + LINE_BREAK);
		StringBuilder line = new StringBuilder();
//		int index = 1;
//		int pedestriansCount = _pedestrianArea.getPedestrians().size();
		for (Pedestrian pedestrian : _pedestrianArea.getPedestrians()) {
			line.append(pedestrian.getId().toString());
			line.append(COMMA);
			Vector2f center = pedestrian.getBody().getCenter();
			line.append(String.format("%.2f", center.x));
			line.append(SPACE);
			line.append(String.format("%.2f", center.y));
			line.append(COMMA);
			Vector2f speed = pedestrian.getBody().getVelocity();
			line.append(String.format("%.2f", speed.x));
			line.append(SPACE);
			line.append(String.format("%.2f", speed.y));
			line.append(COMMA);
			Vector2f futureCenter = pedestrian.getFuture().getBody().getCenter();
			line.append(String.format("%.2f", futureCenter.x));
			line.append(SPACE);
			line.append(String.format("%.2f", futureCenter.y));
			line.append(LINE_BREAK);
//			index++;
		}
		writer.write(line.toString());
	}

	public Supplier<DymaimcFileStep> steps() throws FileNotFoundException {
		final Scanner scanner = new Scanner(new File(buildPath(DYNAMIC_FILE_NAME)));
		return new Supplier<DymaimcFileStep>() {
			@Override
			public DymaimcFileStep get() {
				DymaimcFileStep line = null;
				if (scanner.hasNextLine()) {
					line = new DymaimcFileStep(Float.valueOf(scanner.nextLine()));
					while(scanner.hasNextLine() && !scanner.hasNextFloat()) {
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
						line.pedestrialsInfo().add(lineInfo);
					}
				} else {
					scanner.close();
				}
				return line; 
			}
		};
	}

	private String buildPath(String file) {
		return _directory + "" + File.separatorChar + file;
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
		
		public List<PedestrianDynamicLineInfo> pedestrialsInfo() {
			return _pedestrialsInfo;
		}

	}
	
	public static class PedestrianDynamicLineInfo {
		private int _id;
		private Vector2f _center;
		private Vector2f _velocity;
		private Vector2f _futureCenter;
		
		public PedestrianDynamicLineInfo(int id, Vector2f center, Vector2f velocity,Vector2f futureCenter) {
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
	
	public Float delta() {
		return delta;
	}
}
