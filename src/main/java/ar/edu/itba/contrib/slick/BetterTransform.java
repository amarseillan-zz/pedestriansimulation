package ar.edu.itba.contrib.slick;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import com.google.common.base.Preconditions;

public class BetterTransform {

	private float _scale;
	private float _xOffset, _yOffset;
	private final Transform _transform;
	private boolean inverseDirty;
	private Transform inverse;

	public BetterTransform() {
		inverseDirty = true;
		_transform = new Transform();
		_scale = 1;
	}
	
	private void setInverseDirty() {
		inverseDirty = true;
		inverse = null;
	}
	
	public void scale(float scale) {
		_scale *= scale;
		_transform.concatenate(Transform.createScaleTransform(scale, scale));
		setInverseDirty();
	}
	
	public void translate(float xOffset, float yOffset) {
		_xOffset += xOffset;
		_yOffset += yOffset;
		_transform.concatenate(Transform.createTranslateTransform(xOffset, yOffset));
		setInverseDirty();
	}
	
	public Vector2f transform(Vector2f pt) {
		return _transform.transform(pt);
	}
	
	public Shape transform(Shape shape) {
		return shape.transform(_transform);
	}
	
	public Transform inverse() {
		if (inverseDirty) {
			Preconditions.checkState(_scale != 0);
			inverseDirty = false;
			inverse = new Transform()
				.concatenate(Transform.createScaleTransform(1 / _scale, 1 / _scale))
				.concatenate(Transform.createTranslateTransform(-_xOffset, -_yOffset)); 
		}
		return inverse;
	}
}
