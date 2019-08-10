package mask.registration.alignment;

import mask.registration.Displacement;

class SkipRigidTransform implements RigidTransform {

	@Override
	public Displacement apply(Displacement t) {return t; }

	@Override
	public double getTranslationX() { return 0.0; }

	@Override
	public double getTranslationY() { return 0.0; }

	@Override
	public double getRotation() { return 0.0; }

}
