package ar.edu.itba.pedestriansim.back.logic;

import java.util.List;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.Wall;

import com.google.common.collect.Lists;

public class KillPedestrianStuckOnWall extends PedestrianAreaStep {

	private static final Vector2f tmp = new Vector2f();
	private final List<Pedestrian> _toRemove = Lists.newLinkedList();

	@Override
	public void update(PedestrianArea input) {
		_toRemove.clear();
		for (Pedestrian p : input.pedestrians()) {
			for (Wall wall : input.obstacles()) {
				if (wall.isThick()) {
					Line line = new Line(p.getBody().getCenter(), p.getFuture().getBody().getCenter());
					if (line.intersect(wall.thickBorder(), true, tmp)) {
						System.out.println(" [WARN] Peaton atravesando pared: " + p.getId());
						_toRemove.add(p);
					}
				}
			}
		}
		if (!_toRemove.isEmpty()) {
			input.removePedestrians(_toRemove);
		}
	}

}
