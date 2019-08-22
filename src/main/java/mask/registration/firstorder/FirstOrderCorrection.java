package mask.registration.firstorder;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import mask.registration.alignment.RigidTransform;
import mask.registration.alignment.RigidTransformCalculation;
import mask.registration.displacement.Displacement;
import mask.registration.displacement.SiteSelection;
import mask.registration.distortions.AffineTransform;
import mask.registration.distortions.AffineTransformBuilder;
import mask.registration.distortions.AffineTransformCalculation;
import mask.registration.distortions.AffineTransformCorrection;


public class FirstOrderCorrection implements BiFunction<Collection<Displacement>, FirstOrderSetup, FirstOrderResult> {

	public static FirstOrderResult using(Collection<Displacement> displacements, FirstOrderSetup setup) {
		return new FirstOrderCorrection().apply(displacements, setup);
	}
	
	@Override
	public FirstOrderResult apply(Collection<Displacement> displacements, FirstOrderSetup setup) {
		
		SiteSelection siteSelection = setup.getSiteSelection();
		
		/* 
		 * STEP 1 - Calculate corrected first order based on unaligned data
		 */
		RigidTransform alignment = new RigidTransformCalculation().apply(displacements, siteSelection.getAlignment());
		
		Predicate<Displacement> firstOrderSelector = siteSelection.getCalculation();
		if (setup.getAlignment().equals(Alignments.SCANNER_SELECTED)) {
			firstOrderSelector = siteSelection.getAlignment();
		}
		
		AffineTransform firstOrder = updateFirstOrderForCompensation(setup.getCompensations(),new AffineTransformCalculation()
				.apply(displacements, firstOrderSelector)
				.centerAt(0.0, 0.0));
		
		Collection<Displacement> correctedResults = new AffineTransformCorrection().apply(firstOrder, displacements);
		
		if (setup.getAlignment().equals(Alignments.UNALIGNED)) {
			return new FirstOrderResult(alignment, firstOrder, correctedResults);
		}
		
		/*
		 * STEP 2 - Apply residual rotation correction for custom alignment methods
		 */
		RigidTransform residualAlignment = new RigidTransformCalculation().apply(correctedResults, siteSelection.getAlignment());
		Collection<Displacement> alignedResults = residualAlignment.apply(correctedResults);

		alignment = new RigidTransformCalculation().apply(alignedResults, siteSelection.getCalculation());
		AffineTransform finalFirstOrder = new AffineTransformCalculation()
				.apply(alignedResults, siteSelection.getCalculation())
				.centerAt(0.0, 0.0);
		
		return new FirstOrderResult(alignment, finalFirstOrder, alignedResults);
	}

	private AffineTransform updateFirstOrderForCompensation(Set<Compensations> compensations,
			AffineTransform transform) {
		
		AffineTransformBuilder transformBuilder = new AffineTransformBuilder(transform);
		
		if (compensations.isEmpty()) {
			transformBuilder.disableAll();
			return transformBuilder.build();
		}
		
		if (!compensations.contains(Compensations.ORTHO)) {
			transformBuilder.disableOrthoXY();
		}
		
		if (compensations.contains(Compensations.MAGNIFICATION)) {
			transformBuilder.useMagnification();
		} else if (!compensations.contains(Compensations.SCALE)) {
			transformBuilder.disableScaleXY();
		}
		
		return transformBuilder.build();
	}

}
