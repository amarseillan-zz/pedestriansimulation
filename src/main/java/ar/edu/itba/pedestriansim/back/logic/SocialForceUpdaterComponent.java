package ar.edu.itba.pedestriansim.back.logic;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import ar.edu.itba.pedestriansim.back.entity.Pedestrian;
import ar.edu.itba.pedestriansim.back.entity.PedestrianArea;
import ar.edu.itba.pedestriansim.back.entity.Wall;
import ar.edu.itba.pedestriansim.back.entity.force.DrivingForce;
import ar.edu.itba.pedestriansim.back.entity.force.SpringForceModel;
import ar.edu.itba.pedestriansim.back.entity.physics.Vectors;

public class SocialForceUpdaterComponent extends PedestrianAreaStep {

	private static final float kn = 120000f;
	private static final float kt = 240000f;
	public static float A = 2000;
	public static float B = 0.08f;

	private static final SpringForceModel wallCollisionForceModel = new SpringForceModel();
	private static final DrivingForce drivingForce = new DrivingForce();

	@Override
	public void update(PedestrianArea input) {
		for (Pedestrian pedesitan : input.pedestrians()) {
			pedesitan.getFuture().getBody().setCenter(pedesitan.getBody().getCenter());
			Vector2f force = pedesitan.getBody().getAppliedForce();
			force.set(Vectors.zero());
			force.add(fSocial(pedesitan, input.pedestrians())).add(fCollision(pedesitan, input.obstacles())).add(fDesire(pedesitan));
		}
	}

	private Vector2f fSocial(Pedestrian pi, Iterable<Pedestrian> others) {
		Vector2f force = new Vector2f();
		Vector2f center = pi.getBody().getCenter();
		float ri = pi.getBody().getRadius();
		for (Pedestrian pj : others) {
			if (pi == pj) {
				continue;
			}
			float rj = pj.getBody().getRadius();
			Vector2f rij = pj.getBody().getCenter().copy().sub(center);
			float d = rij.length();
			Vector2f en = rij.copy().normalise();
			Vector2f et = new Vector2f(en.y, -en.x);
			float eps = d - (ri + rj);
			if (eps < 0) {
				// Contact force
				Vector2f fContactn = en.copy().scale(-eps * kn);
				float vt = pi.getBody().getVelocity().dot(et);
				// TODO: esta bien usar la velocidad?
				Vector2f fContactt = et.copy().scale(vt * eps * kt);
				fContactn.add(fContactt).scale(g(eps));
				force.add(fContactn);
			} else {
				// Social force
				float exp = (float) Math.exp(-eps / B);
				Vector2f fSocial = en.copy().scale(-exp * A);
				force.add(fSocial);
			}
		}
		return force;
	}

	private float g(float eps) {
		return eps;
	}

	private Vector2f fCollision(Pedestrian pedesitan, Iterable<Wall> walls) {
		Vector2f f = new Vector2f();
		for (Wall wall : walls) {
			Line line = wall.line();
			if (pedesitan.getShape().intersects(line)) {
				f.add(wallCollisionForceModel.getForce(pedesitan.getBody(), line));
			}
		}
		return f;
	}

	private Vector2f fDesire(Pedestrian p1) {
		Vector2f center = p1.getBody().getCenter();
		Vector2f target = p1.getTargetSelection().getTarget().getClosesPoint(center);
		return drivingForce.getForce(p1.getBody(), target, p1.getMaxVelocity());
	}
}
