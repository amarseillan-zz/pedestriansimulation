package ar.edu.itba.pedestriansim.back.component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.newdawn.slick.geom.Shape;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.metric.CollitionCount;
import ar.edu.itba.pedestriansim.back.metric.CollitionCountPerInstant;
import ar.edu.itba.pedestriansim.back.physics.Collitions;

import com.google.common.collect.Lists;

public class MetricsComponent extends Component{
	
	private PedestrianArea scene;
	private File outputFile;
	private CollitionCountPerInstant collitionCountPerInstant;
	private CollitionCount collitionCount;

	public MetricsComponent(PedestrianArea scene, File file) {
		this.scene = scene;
		collitionCountPerInstant = new CollitionCountPerInstant();
		collitionCount = new CollitionCount();
		this.outputFile = file;
	}
	
	@Override
	public void update(float elapsedTimeInSeconds) {
		List<Pedestrian> pedestrians = Lists.newArrayList(scene.getPedestrians());
		collitionCountPerInstant.onIterationStart();
		collitionCount.onIterationStart();
		for (int i = 0; i<pedestrians.size(); i++) {
			for (int j = i+1; j<pedestrians.size(); j++) {
				Pedestrian p1 = pedestrians.get(i);
				Pedestrian p2 = pedestrians.get(j);
				Shape cs1 = p1.getBody().getCollitionShape().getShape();
				Shape cs2 = p2.getBody().getCollitionShape().getShape();
				if (Collitions.touching(cs1, cs2)) {
					collitionCountPerInstant.update(elapsedTimeInSeconds, p1.getId(), p2.getId());
					collitionCount.update(elapsedTimeInSeconds, p1.getId(), p2.getId());
				}
			}
		}
		collitionCountPerInstant.onIterationEnd();
		collitionCount.onIterationEnd();
	}
	
	@Override
	public void onEnd() {
		super.onEnd();
		FileWriter writer;
		try {
			writer = new FileWriter(outputFile);
			collitionCountPerInstant.appendResults(writer);
			collitionCount.appendResults(writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
