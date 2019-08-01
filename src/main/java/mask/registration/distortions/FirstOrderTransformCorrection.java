package mask.registration.distortions;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import mask.registration.Displacement;
import mask.registration.alignment.RigidCorrection;
import mask.registration.alignment.RigidTransform;
import mask.registration.alignment.RigidTransformCalculation;
import mask.registration.alignment.TranslateToCenter;

public class FirstOrderTransformCorrection implements Function<Collection<Displacement>, List<Displacement>> {

	private final RigidTransform alignment;
	
	private final AffineTransform firstOrder;
	
	public FirstOrderTransformCorrection(RigidTransform alignment, AffineTransform firstOrder) {
		this.alignment = alignment;
		this.firstOrder = firstOrder;
	}
	
	@Override
	public List<Displacement> apply(Collection<Displacement> t) {
		
		
		RigidCorrection alignmentFunction = new RigidCorrection();
		TranslateToCenter translateToCenter = new TranslateToCenter();
		
		List<Displacement> aligned = alignmentFunction
										.andThen(translateToCenter)
										.apply(alignment, t);
	
		/* 
		 * TODO:
		 * Here after alignment, correct only scale & shear (use only 4 params)
		 * But now the name is no longer appropriate.
		 */
		AffineTransformCorrection affineCorrection = new AffineTransformCorrection();
		List<Displacement> corrected = affineCorrection.apply(firstOrder, aligned);
		
		RigidTransform residualAlignment = new RigidTransformCalculation().apply(corrected, d->true);
		List<Displacement> finalCorrected = alignmentFunction
				.andThen(translateToCenter.reverse())
				.apply(residualAlignment, corrected);
		
		return finalCorrected;
	}

	

}
