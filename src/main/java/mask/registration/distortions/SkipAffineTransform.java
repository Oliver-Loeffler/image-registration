package mask.registration.distortions;

import mask.registration.Displacement;

public class SkipAffineTransform extends AffineTransform {

	public SkipAffineTransform() {
		super(0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
	}

	@Override
	public Displacement apply(Displacement t) {
		return t;
	}

}
