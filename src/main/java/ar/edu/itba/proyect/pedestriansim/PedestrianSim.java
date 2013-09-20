package ar.edu.itba.proyect.pedestriansim;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import ar.edu.itba.proyect.pedestriansim.component.ExternalForcesComponent;
import ar.edu.itba.proyect.pedestriansim.component.LocationUpdater;
import ar.edu.itba.proyect.pedestriansim.component.Updateable;

public class PedestrianSim extends BasicGame {

	private final static int INTERVAL_UPDATE_MILLIS = 200;
	
	public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new PedestrianSim("Pedestrian simulation"));
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}

	private int timeFromLastUpdateMillis = 0; 
	private ShapeRenderer renderer = new ShapeRenderer();
	private Scene scene = new Scene();
	private List<Updateable> updatables = new LinkedList<Updateable>();

	public PedestrianSim(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		float pedestrianMass = 50f;
		scene.addPedestrian(new Pedestrian(new Circle(100, 100, 10), pedestrianMass));
		updatables.add(new ExternalForcesComponent(scene));
		updatables.add(new LocationUpdater(scene));
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		renderer.setColor(Color.blue);
		for (Shape shape : scene.getObstacles()) {
			renderer.render(g, shape);
		}
		renderer.setColor(Color.green);
		for (Pedestrian pedestrian : scene.getPedestrians()) {
			renderer.render(g, pedestrian.getShape());
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		timeFromLastUpdateMillis += delta;
		if (timeFromLastUpdateMillis >= INTERVAL_UPDATE_MILLIS) {
			float seconds = timeFromLastUpdateMillis / 1000f;
			for (Updateable updatable : updatables) {
				updatable.update(gc, seconds);
			}
			timeFromLastUpdateMillis = 0;
		}
	}
}
