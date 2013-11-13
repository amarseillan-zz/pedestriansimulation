package ar.edu.itba.pedestriansim.back;

import org.newdawn.slick.geom.Vector2f;

public class PedestrianFuture {

	private Pedestrian _owner;
	private RigidBody _body;

	public PedestrianFuture(Pedestrian owner) {
		_owner = owner;
		_body = new LimitedRangeRigidBody(1, new Vector2f(), 0.1f);
	}

	public RigidBody getBody() {
		return _body;
	}

	private class LimitedRangeRigidBody extends RigidBody {

		private final Vector2f directorCache = new Vector2f();

		public LimitedRangeRigidBody(float mass, Vector2f location, float radius) {
			super(mass, location, radius);
		}

		@Override
		public void apply(Vector2f deltaVelocity, Vector2f deltaPosition) {
			super.apply(deltaVelocity, deltaPosition);
			float distance = getCenter().distance(_owner.getBody().getCenter());
			float velocity = _owner.getMaxVelocity();
			if (distance > velocity) {
				directorCache.set(getCenter());
				directorCache.sub(_owner.getBody().getCenter()).normalise();
				getCenter().set(_owner.getBody().getCenter());
				getCenter().add(directorCache.scale(velocity));
			}
		}
	}
}
