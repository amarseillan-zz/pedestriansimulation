package ar.edu.itba.pedestriansim.back.entity.force;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Wall;
import ar.edu.itba.pedestriansim.back.entity.physics.Collitions;
import ar.edu.itba.pedestriansim.back.entity.physics.RigidBody;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class SpringForceModel {

	private static final float WALL_MAGIC = 10;
	private final Vector2f cache = new Vector2f();

	private final float _K = 10000;
	private final Vector2f closestPointCache = new Vector2f();

	public Vector2f getForce(RigidBody body1, RigidBody body2) {
		Circle shape1 = body1.getShape();
		Circle shape2 = body2.getShape();
		if (Collitions.touching(shape1, shape2)) {
			float overlapping = Collitions.overlapping(shape1, shape2);
			cache.set(body1.getCenter());
			Vector2f director = cache.sub(body2.getCenter()).normalise();
			return director.scale(_K * overlapping);
		}
		return Vectors.zero();
	}

	public Vector2f getForce(RigidBody body, Wall wall) {
		Vector2f center = body.getCenter();
		Line line = wall.line();
		if (wall.isThick()) {
			line.getClosestPoint(center, closestPointCache);
			Vector2f wallDir = closestPointCache.copy().sub(center);
			Vector2f wallDirNorm = wallDir.normalise();
			float dot = wallDirNorm.dot(wall.normal().get());
			float overlapping = Collitions.overlapping(body.getShape(), line);
			if (dot == 1 && overlapping == 0) {
				overlapping = body.getRadius();	// should be * 2
			}
			return springCollition(overlapping, wallDir.scale(-1));
		} else {
			float overlapping = Collitions.overlapping(body.getShape(), line);
			if (overlapping > 0) {
				line.getClosestPoint(center, closestPointCache);
				return springCollition(overlapping, center.copy().sub(closestPointCache));
			}
		}
		return Vectors.zero();
	}

	private Vector2f springCollition(float overlapping, Vector2f dir) {
		return dir.scale(_K * overlapping * WALL_MAGIC);
	}

}
