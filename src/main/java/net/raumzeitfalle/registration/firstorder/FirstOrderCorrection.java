package net.raumzeitfalle.registration.firstorder;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import net.raumzeitfalle.registration.alignment.RigidTransform;
import net.raumzeitfalle.registration.alignment.RigidTransformCalculation;
import net.raumzeitfalle.registration.displacement.Displacement;
import net.raumzeitfalle.registration.displacement.SiteSelection;
import net.raumzeitfalle.registration.distortions.AffineTransform;
import net.raumzeitfalle.registration.distortions.AffineTransformBuilder;
import net.raumzeitfalle.registration.distortions.AffineTransformCalculation;
import net.raumzeitfalle.registration.distortions.AffineTransformCorrection;

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
		
		/*
		 * STEP 2 - Calculate the 6-parameter model (FirstOrder) and parametrize according to corrections defined in setup
		 */
		AffineTransform calculatedFirstOrder = new AffineTransformCalculation().apply(displacements, firstOrderSelector);
		AffineTransform firstOrder = updateFirstOrderForCompensation(setup.getCompensations(),calculatedFirstOrder);
		
		/*
		 * STEP 3 - Apply all requested compensations
		 */
		Collection<Displacement> correctedResults = new AffineTransformCorrection().apply(firstOrder, displacements);
		
		/*
		 * When no alignment is requested, then the work is done here. The result will be returned then.
		 */
		if (setup.getAlignment().equals(Alignments.UNALIGNED)) {
			return new FirstOrderResult(alignment, firstOrder, correctedResults);
		}
		
		/*
		 * STEP 4 - When any case of alignment is requested, then a 2nd correction is needed.
		 * Calculate and correct residual rotation correction for custom alignment methods
		 * 
		 */
		RigidTransform residualAlignment = new RigidTransformCalculation().apply(correctedResults, siteSelection.getAlignment());
		Collection<Displacement> alignedResults = residualAlignment.apply(correctedResults);

		alignment = new RigidTransformCalculation().apply(alignedResults, siteSelection.getCalculation());
		AffineTransform finalFirstOrder = new AffineTransformCalculation()
											.apply(alignedResults, siteSelection.getCalculation())
											.centerAt(0.0, 0.0);
		
		return new FirstOrderResult(alignment, finalFirstOrder, alignedResults);
	}

	
	/**
	 * In order to correct a certain transform, this method ensures that depending on given set of compensations,
	 * the proper values are selected and set in a given {@link AffineTransform}.
	 * 
	 * @param compensations {@link Set}t of {@link Compensations} to apply.
	 * @param transform Calculated {@link AffineTransform}
	 * @return {@link AffineTransform} prepared on order to perform requested {@link Compensations}
	 */
	private AffineTransform updateFirstOrderForCompensation(Set<Compensations> compensations,
			AffineTransform transform) {
		
		AffineTransformBuilder transformBuilder = new AffineTransformBuilder(transform.centerAt(0.0, 0.0));
		
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
