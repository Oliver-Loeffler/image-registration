package mask.registration.distortions;

import mask.registration.Displacement;

/**
 * The {@link SkipAffineTransform} is ised in cases where an {@link AffineTransform} is integrated into a processing pipeline but no modification to given data is expeced as all affine transform parameters have been set to zero (0.0).
 * 
 * @author oliver
 *
 */
public class SkipAffineTransform extends AffineTransform {

	public SkipAffineTransform() {
		super(0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d);
	}

	@Override
	public Displacement apply(Displacement t) {
		return t;
	}

}
